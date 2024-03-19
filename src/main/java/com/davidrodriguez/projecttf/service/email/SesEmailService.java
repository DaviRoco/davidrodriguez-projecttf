package com.davidrodriguez.projecttf.service.email;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.davidrodriguez.projecttf.dto.EmailMessage;
import com.davidrodriguez.projecttf.dto.UserDto;
import com.davidrodriguez.projecttf.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
@Service
public class SesEmailService implements EmailService {

  private AmazonSimpleEmailService client;
  private final Regions region = Regions.US_EAST_1;
  private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final String INTRO_EMAIL_TEMPLATE_NAME = "IntroEmailTemplate";
  private static final String RECOVERY_EMAIL_TEMPLATE_NAME = "RecoveryEmailTemplate";

  @Override
  public boolean send(EmailMessage emailMessage) {
    try {
      if (client == null) {
        client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(region).build();
      }
      var request = new SendEmailRequest()
          .withDestination(new Destination().withToAddresses(emailMessage.getTo()))
          .withMessage(new Message()
              .withBody(new Body()
                  .withHtml(new Content()
                      .withCharset("UTF-8").withData(emailMessage.getContent())))
              .withSubject(new Content()
                  .withCharset("UTF-8").withData(emailMessage.getSubject())))
          .withSource(emailMessage.getFrom());
      client.sendEmail(request);
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
      return false;
    }
    return true;
  }
  @Override
  public boolean sendIntroEmail(String to, UserDto userDto) {
    String subject = "¡Bienvenido a Farmaticas!";
    String content = "<html><body><p><h1> Estimado " + userDto.getFirstName() + "</h1></p><p>¡Gracias por unirte a Farmaticas! Estamos emocionados de tenerte a bordo.</p>" +
            "Haz clic en el siguiente enlace para establecer tu contraseña:</p><p><a href='http://davidrodriguez-projecttf.s3-website-us-east-1.amazonaws.com/#/change-password?email=" + userDto.getEmail() + "'>Establecer contraseña</a></p>" +
            "<p>Saludos cordiales,<br> David Rodríguez Coto</p></body></html>";
    return send(createEmailMessage(to, subject, content));
  }
  @Override
  public boolean sendRecoveryEmail(String to, UserDto userDto) {
    String subject = "Instrucciones para recuperar la contraseña";
    String content = "<html><body><p><h1> Estimado" + userDto.getFirstName() + "</h1></p>" +
            "<p>Hemos recibido una solicitud para restablecer tu contraseña. Haz clic en el siguiente enlace para restablecerla:</p>" +
            "<p><a href='http://davidrodriguez-projecttf.s3-website-us-east-1.amazonaws.com/#/change-password?email=" + userDto.getEmail() + "'>Restablecer contraseña</a></p><p>Si no solicitaste este cambio, por favor ignora este correo electrónico.</p>" +
            "<p>Saludos cordiales,<br> David Rodríguez Coto</p></body></html>";
    return send(createEmailMessage(to, subject, content));
  }

  private EmailMessage createEmailMessage(String to, String subject, String content) {
    return new EmailMessage(to, "david@rodriguezcoto.com", subject, content);
  }
}
