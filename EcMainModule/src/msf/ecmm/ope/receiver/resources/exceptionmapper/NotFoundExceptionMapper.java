package msf.ecmm.ope.receiver.resources.exceptionmapper;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import msf.ecmm.ope.receiver.ReceiverDefinitions;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

	@Override
	public Response toResponse(NotFoundException exception) {
		return Response.status(Status.NOT_FOUND).entity(ReceiverDefinitions.NOT_FOUND_ERROR_CODE).build();
	}
}
