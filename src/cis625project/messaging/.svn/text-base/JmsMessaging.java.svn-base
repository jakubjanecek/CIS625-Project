package cis625project.messaging;

import java.io.Serializable;
import java.util.Random;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQObjectMessage;

/**
 * @author platinix
 */
public class JmsMessaging {

  private static JmsMessaging instance = new JmsMessaging ();
  private static final Random r = new Random ();
  private ActiveMQConnectionFactory connectionFactory = null;
  private Connection connection = null;
  private Session session = null;
  private MessageProducer mdProd = null;
  private MessageProducer emrProd = null;
  private MessageConsumer mdCons = null;
  private MessageConsumer emrCons = null;

  private JmsMessaging () {
    try {
      connectionFactory = new ActiveMQConnectionFactory ("tcp://localhost:61616");
      connection = connectionFactory.createConnection ();
      connection.start ();
      session = connection.createSession (false, Session.AUTO_ACKNOWLEDGE);
      Destination destination1 = session.createQueue ("mdQueue");
      Destination destination2 = session.createQueue ("emrQueue");
      mdProd = session.createProducer (destination1);
      emrProd = session.createProducer (destination2);
      mdCons = session.createConsumer (destination1);
      emrCons = session.createConsumer (destination2);
    }
    catch (JMSException ex) {
      ex.printStackTrace ();
    }
  }

  public static JmsMessaging getInstance () {
    return instance;
  }

  public void sendToMd (Message m) {
    try {
      javax.jms.ObjectMessage jmsMsg = new ActiveMQObjectMessage ();
      jmsMsg.setObject ((Serializable) m);
      mdProd.send (jmsMsg);
    }
    catch (JMSException ex) {
      ex.printStackTrace ();
    }
  }

  public Message receiveFromMd () {
    try {
      javax.jms.ObjectMessage jmsMsg = (ObjectMessage) mdCons.receive ();
      return (Message) jmsMsg.getObject ();
    }
    catch (JMSException ex) {
      ex.printStackTrace ();
    }
    return null;
  }

  public void sendToEmr (Message m) {
    try {
      javax.jms.ObjectMessage jmsMsg = new ActiveMQObjectMessage ();
      jmsMsg.setObject ((Serializable) m);
      emrProd.send (jmsMsg);
    }
    catch (JMSException ex) {
      ex.printStackTrace ();
    }
  }

  public Message receiveFromEmr () {
    try {
      javax.jms.ObjectMessage jmsMsg = (ObjectMessage) emrCons.receive ();
      return (Message) jmsMsg.getObject ();
    }
    catch (JMSException ex) {
      ex.printStackTrace ();
    }
    return null;
  }

  public Channel getPrivateChannel () {
    return new JmsChannelImpl ("privateChannel" + String.valueOf (r.nextInt ()));
  }

  public String getPrivateChannelName () {
    return "privateChannel" + String.valueOf (r.nextInt ());
  }

  public Channel getPrivateChannelByName (String name) {
    return new JmsChannelImpl (name);
  }

  public class JmsChannelImpl implements Channel {

    private MessageProducer prod = null;
    private MessageConsumer cons = null;

    public JmsChannelImpl (String name) {
      try {
        Destination q = session.createQueue (name);
        prod = session.createProducer (q);
        cons = session.createConsumer (q);
      }
      catch (JMSException ex) {
        ex.printStackTrace ();
      }
    }

    public void send (Message m) {
      try {
        javax.jms.ObjectMessage jmsMsg = new ActiveMQObjectMessage ();
        jmsMsg.setObject ((Serializable) m);
        prod.send (jmsMsg);
      }
      catch (JMSException ex) {
        ex.printStackTrace ();
      }
    }

    public Message receive () {
      try {
        javax.jms.ObjectMessage jmsMsg = (ObjectMessage) cons.receive ();
        return (Message) jmsMsg.getObject ();
      }
      catch (JMSException ex) {
        ex.printStackTrace ();
      }
      return null;
    }
  }
}
