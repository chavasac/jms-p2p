package com.sachin.jms.hm.eligibilitycheck;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.sachin.jms.hm.eligibilitycheck.listener.EligibilityCheckListener;

public class EligibilityCheckerApp {

	public static void main(String[] args) throws NamingException, JMSException, InterruptedException {
		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
		
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		JMSContext jmsContext = connectionFactory.createContext();
		
		/*
		 * Uncomment below piece of code to see how listener works with JMS
		 */
		/*
		 * JMSConsumer consumer = jmsContext.createConsumer(requestQueue);
		 * consumer.setMessageListener(new EligibilityCheckListener());
		 */
		
		JMSConsumer consumer1 = jmsContext.createConsumer(requestQueue);
		JMSConsumer consumer2 = jmsContext.createConsumer(requestQueue);
		
		for (int i = 0; i < 5; i++) {
			System.out.println("Consumer 1 : " + consumer1.receive());
			System.out.println("Consumer 2 : " + consumer2.receive());
		}
		
		Thread.sleep(10000);
		
	}

}
