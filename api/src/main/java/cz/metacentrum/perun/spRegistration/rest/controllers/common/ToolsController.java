package cz.metacentrum.perun.spRegistration.rest.controllers.common;

import cz.metacentrum.perun.spRegistration.persistence.configs.AppConfig;
import cz.metacentrum.perun.spRegistration.rest.ApiUtils;
import cz.metacentrum.perun.spRegistration.service.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;
import java.util.Collections;
import java.util.Map;

/**
 * Tools controller
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
@RestController
public class ToolsController {

	@Autowired
	private AppConfig appConfig;

	@PostMapping(path = "/api/tools/encrypt")
	public Map<String, String> encrypt(@RequestBody String toEncrypt)
			throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
		if (toEncrypt == null) {
			return null;
		}

		toEncrypt = ApiUtils.normalizeRequestBodyString(toEncrypt);

		String val = ServiceUtils.encrypt(toEncrypt, appConfig.getSecret());
		return Collections.singletonMap("value", val);
	}

	@PostMapping(path = "/api/tools/decrypt")
	public Map<String, String> decrypt(@RequestBody String toDecrypt)
			throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
		if (toDecrypt == null) {
			return null;
		}

		toDecrypt = ApiUtils.normalizeRequestBodyString(toDecrypt);

		String val = ServiceUtils.decrypt(toDecrypt, appConfig.getSecret());
		return Collections.singletonMap("value", val);
	}
}