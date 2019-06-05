package com.sachin.jms.hm.eligibilitycheck.listener;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.sachin.jms.hm.domain.Patient;

public class EligibilityCheckListener implements MessageListener {

	public void onMessage(Message message) {
		ObjectMessage objectMessage = (ObjectMessage) message;
		try {
			InitialContext initialContext = new InitialContext();
			Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");
			
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
			JMSContext jmsContext = connectionFactory.createContext();
			MapMessage mapMessage = jmsContext.createMapMessage();
			
			Patient patient = (Patient) objectMessage.getObject();
			System.out.println("Insurance provider is : " + patient.getInsuranceProvider());
			String insuranceProvider = patient.getInsuranceProvider();
			if (insuranceProvider.equals("Blue Cross Blue Shield") || insuranceProvider.equals("United Insurance")) {
				if (patient.getCopay() < 40 && patient.getAmountTobePaid() < 1000) {
					mapMessage.setBoolean("eligible", true);
				}
			} else {
				mapMessage.setBoolean("eligible", false);
			}
			
			JMSProducer producer = jmsContext.createProducer();
			producer.send(replyQueue, mapMessage);
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
