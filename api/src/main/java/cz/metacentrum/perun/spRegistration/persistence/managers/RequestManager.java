package cz.metacentrum.perun.spRegistration.persistence.managers;

import cz.metacentrum.perun.spRegistration.persistence.enums.RequestAction;
import cz.metacentrum.perun.spRegistration.persistence.enums.RequestStatus;
import cz.metacentrum.perun.spRegistration.persistence.models.Request;
import cz.metacentrum.perun.spRegistration.persistence.models.RequestApproval;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Set;

/**
 * Interface for working with the Request in the database.
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
public interface RequestManager {

	void setJdbcTemplate(JdbcTemplate template);

	/**
	 * Create request in DB.
	 * @param request Request object to be stored.
	 * @return Generated ID of stored Request.
	 */
	Long createRequest(Request request);

	/**
	 * Update request in DB.
	 * @param request Request object with updated data.
	 * @return True if everything went OK.
	 */
	boolean updateRequest(Request request);

	/**
	 * Delete request from DB.
	 * @param reqId ID of Request to be deleted.
	 * @return True if everything went OK.
	 */
	boolean deleteRequest(Long reqId);

	/**
	 * Get request specified by ID.
	 * @param reqId ID of request.
	 * @return Found Request object.
	 */
	Request getRequestByReqId(Long reqId);

	/**
	 * Get all requests from DB.
	 * @return List of found Request objects.
	 */
	List<Request> getAllRequests();

	/**
	 * Get all requests from DB where specified user is a requester.
	 * @param userId ID of user.
	 * @return List of found Request objects.
	 */
	List<Request> getAllRequestsByUserId(Long userId);

	/**
	 * Get all requests from DB with specified status.
	 * @param status Status of Requests.
	 * @return List of found Request objects.
	 */
	List<Request> getAllRequestsByStatus(RequestStatus status);

	/**
	 * Get all requests from DB with specified action.
	 * @param action Action of Request.
	 * @return List of found Request objects.
	 */
	List<Request> getAllRequestsByAction(RequestAction action);

	/**
	 * Get all requests from DB associated with facility specified by ID.
	 * @param facilityId ID of associated facility.
	 * @return List of found Request objects.
	 */
	Request getRequestByFacilityId(Long facilityId);

	/**
	 * Get all requests from DB associated with facilities specified by IDs.
	 * @param facilityIds IDs of associated facilities.
	 * @return List of found Request objects.
	 */
	List<Request> getAllRequestsByFacilityIds(Set<Long> facilityIds);

	/**
	 * Add signature of transferring to the production approval to the request
	 * @param requestId id of request for transfer
	 * @param userId id of signing user
	 * @param fullName full name of signing user from Perun
	 * @param approvalName name entered by signing user
	 * @return true if all went OK, false otherwise.
	 */
	boolean addSignature(Long requestId, Long userId, String fullName, String approvalName);

	/**
	 * Get all approvals for transferring of service into production environment
	 * @param requestId id of transfer request
	 * @return List of associated approvals
	 */
	List<RequestApproval> getApprovalsForRequest(Long requestId);
}
