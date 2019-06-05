package com.sachin.jms.hm.clinicals;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.sachin.jms.hm.domain.Patient;

public class ClinicalsApp {

	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
		Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");
		
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		JMSContext jmsContext = connectionFactory.createContext();
		
		JMSProducer producer = jmsContext.createProducer();
		
		ObjectMessage message = jmsContext.createObjectMessage();
		Patient patient = new Patient();
		patient.setId(1);
		patient.setName("Sameer");
		patient.setInsuranceProvider("Blue Cross Blue Shield");
		patient.setCopay(30d);
		patient.setAmountTobePaid(500d);
		
		message.setObject(patient);
		
		/*
		 * Uncomment below line to see how the app works with listener registered with consumer
		 */
		//producer.send(requestQueue, message);
		
		/*
		 * Below loop is added to produce multiple message to demonstrate load balancing with consumer
		 */
		for (int i = 0; i < 10; i++) {
			producer.send(requestQueue, message);
		}
		
		JMSConsumer consumer = jmsContext.createConsumer(replyQueue);
		MapMessage replyMessage = (MapMessage) consumer.receive(30000);
		System.out.println("Is Patient Eligible : " + replyMessage.getBoolean("eligible"));
		
	}

}
