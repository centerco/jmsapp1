package ru.chuchalov.messaging.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.JsonPath;

import ru.chuchalov.messaging.PHMessage;
import ru.chuchalov.messaging.PHMessageBuilder;

/**
 * Юнит-тест для тестирования методов обработки сущностей {@link PHMessage}
 * Реализован цикл работы с сущностями:
 * - юнит-тест методов класса сущности {@link PHMessage} {@link JmsApp1Test#phMessageTest()}
 * - юнит-тест методов класса контроллера {@link PHMessageController} {@link JmsApp1Test#phMessageAddRecordsTest()}
 * 
 * @author Andrei Chuchalov
 * @version 1.0
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@PropertySource("classpath:application.properties")
public class JmsApp1Test {
	
	private static final String[] COUNTER = {"First ", "Second ", "Third "};
	private static final String TEST_MESSAGE = "test message";
	private static final String MSM = "Message sent to ESB.";
	
	private static final String PHM_PATH = "/api/messages";
	private static final String ERROR_PATH = "/error";
	private static final String INVALID_PATH = "/invalid";
	private static final String SEND_PATH = "/send";
	
	private static Logger log = LoggerFactory.getLogger(JmsApp1Test.class);
	
	private String uid = null;
	
	@Autowired
	private MockMvc mvc;
	
	/**
	 * Метод проверяет логику работы методов сущности {@link PHMessage}:<br>
	 * - создание с параметром<br>
	 * - создание с помощью строителя<br>
	 * - изменение свойств<br>
	 * - сравнение разных объектов<br>
	 * - сравнение идентичных объектов<br>
	 */
	@Test
	public void phMessageTest() {
		PHMessage msg1 = new PHMessage(TEST_MESSAGE);
		PHMessage msg2 = PHMessageBuilder.create().withMessage(TEST_MESSAGE).build();
		
		msg1.setId(msg2.getId());
		msg1.setValid(false);
		
		Assert.assertNotNull(msg1);
		Assert.assertNotNull(msg2);
		
		Assert.assertNotEquals(msg1, TEST_MESSAGE);
		Assert.assertNotEquals(msg1, msg2);
		
		msg1.setValid(true);
		
		Assert.assertEquals(msg1, msg2);
	}
	
	/**
	 * Метод проверяет логику работы методов контроллера {@link PHMessageController}:<br>
	 * - добавление сущностей<br>
	 * - добавление сущности с использованием невалидной информации<br>
	 * - поиск всех сущностей<br>
	 * - поиск сущностей по идентификатору<br>
	 * - удаление сущности по идентификатору<br>
	 * - поиск удаленной сущности по идентификатору<br>
	 * - удаление сущности<br>
	 * - модификация сущностей в очереди<br>
	 * @throws Exception
	 */
	@Test
	public void phMessageAddRecordsTest() throws Exception {
		for(String s : COUNTER)
		this.mvc.perform(post(PHM_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"message\":\""+s+TEST_MESSAGE+"\"}"))
				.andDo(print())
				.andExpect(status().isCreated());	
		
		this.mvc.perform(post(PHM_PATH)
				.content("Request for forcing error response!"))
				.andDo(print())
				.andExpect(status().isBadRequest());

		this.mvc.perform(get(PHM_PATH)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].message").value(COUNTER[0]+TEST_MESSAGE))
				.andExpect(jsonPath("$[1].message").value(COUNTER[1]+TEST_MESSAGE))
				.andExpect(jsonPath("$[2].message").value(COUNTER[2]+TEST_MESSAGE))
				.andDo(mvcResult -> {
					String json = mvcResult.getResponse().getContentAsString();
					uid = JsonPath.parse(json).read("$[0].id").toString();
				});
		
		log.info(">> We will look for: "+uid);
		this.mvc.perform(get(PHM_PATH+"/"+uid)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(uid));
		
		this.mvc.perform(patch(PHM_PATH+ERROR_PATH+"/"+uid)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
		
		this.mvc.perform(patch(PHM_PATH+INVALID_PATH+"/"+uid)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
		
		this.mvc.perform(get(PHM_PATH+"/"+uid)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(uid))
				.andExpect(jsonPath("$.error").value(true))
				.andExpect(jsonPath("$.valid").value(false));
		
		log.info(">> We will now delete selected message: "+uid);
		this.mvc.perform(delete(PHM_PATH+"/"+uid)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNoContent());
		
		this.mvc.perform(delete(PHM_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"message\":\""+COUNTER[2]+TEST_MESSAGE+"\"}"))
				.andDo(print())
				.andExpect(status().isNoContent());
		
		log.info(">> We will now try to look for deleted item: "+uid);
		this.mvc.perform(get(PHM_PATH+"/"+uid)
		 		.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());

		this.mvc.perform(get(PHM_PATH+"/"+SEND_PATH))
				.andDo(print())
				.andExpect(content().string(MSM));
	}

}
