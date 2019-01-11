package it.olegna.template.application.resource.server.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.olegna.template.web.dto.BaseResponse;

@Controller
@RequestMapping("/esempio")
public class SampleController {

	private static final Logger logger = LoggerFactory.getLogger(SampleController.class.getName());
	
	@RequestMapping(value= {"/recupera"}, method= {RequestMethod.GET}, produces= {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<BaseResponse<List<Messaggio>>> getMessaggi(){
		BaseResponse<List<Messaggio>> result = new BaseResponse<>();
		String descrizioneOperazione = null;
		HttpStatus status = null;
		try {
			List<Messaggio> body = new ArrayList<>(5);
			for( int i = 0; i < 5; i++ )
			{
				body.add(new Messaggio("testoMessaggio + "+i));
			}
			status = HttpStatus.OK;
			descrizioneOperazione="Recupero informazioni OK";
			result.setNumeroOggettiRestituiti(body.size());
			result.setNumeroOggettiTotali(body.size());
		} catch (Exception e) {
			descrizioneOperazione = "Errore nel recupero delle informazioni richieste; "+e.getMessage();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logger.error(descrizioneOperazione, e);
		}
		result.setDescrizioneOperazione(descrizioneOperazione);
		result.setEsitoOperazione(status.value());
		return ResponseEntity.status(status).body(result);
		
	}
	
	class Messaggio
	{
		private String id;
		private String testoMessaggio;
		
		public Messaggio() {
			super();
		}
		
		public Messaggio(String testoMessaggio) {
			super();
			this.testoMessaggio = testoMessaggio;
			this.id = UUID.randomUUID().toString();
		}

		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getTestoMessaggio() {
			return testoMessaggio;
		}
		public void setTestoMessaggio(String testoMessaggio) {
			this.testoMessaggio = testoMessaggio;
		}
		
	}
}
