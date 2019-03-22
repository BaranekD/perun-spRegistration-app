package cz.metacentrum.perun.spRegistration.rest.controllers.requests;

import cz.metacentrum.perun.spRegistration.persistence.models.Request;
import cz.metacentrum.perun.spRegistration.persistence.models.User;
import cz.metacentrum.perun.spRegistration.service.UserCommandsService;
import cz.metacentrum.perun.spRegistration.service.exceptions.SpRegistrationApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
public class CommonRequestsController {

	private static final Logger log = LoggerFactory.getLogger(CommonRequestsController.class);

	private final UserCommandsService service;

	@Autowired
	public CommonRequestsController(UserCommandsService service) {
		this.service = service;
	}

	@GetMapping(path = "/api/request/{requestId}")
	public Request requestDetail(@SessionAttribute("user") User user,
								 @PathVariable("requestId") Long requestId) throws SpRegistrationApiException {
		log.debug("requestDetail(user: {}, requestId: {})", user.getId(), requestId);
		try {
			return service.getDetailedRequest(requestId, user.getId());
		} catch (Exception e) {
			throw new SpRegistrationApiException(e);
		}
	}
}
