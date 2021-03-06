/* Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.resources.exceptionmapper;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.log.MsfLogger;
import msf.ecmm.ope.receiver.ReceiverDefinitions;

/**
 * RestServerExceptionMapper
 */
@Provider
public class RestServerExceptionMapper implements ExceptionMapper<Throwable> {

  /**
   * Logger.
   */
  protected static final MsfLogger logger = new MsfLogger();

  /**
   * Response Update Process.
   *
   * @param exception
   *          Exception
   * @return response after update
   */
  @Override
  public Response toResponse(Throwable exception) {
    logger.trace(CommonDefinitions.START);
    logger.debug("Occured exception.", exception);

    Status status = null;
    if (exception instanceof BadRequestException) {
      status = Status.BAD_REQUEST;
    } else if (exception instanceof ForbiddenException) {
      status = Status.FORBIDDEN;
    } else if (exception instanceof NotFoundException) {
      status = Status.NOT_FOUND;
    } else if (exception instanceof NotAllowedException) {
      status = Status.METHOD_NOT_ALLOWED;
    } else if (exception instanceof NotAcceptableException) {
      status = Status.NOT_ACCEPTABLE;
    } else if (exception instanceof NotSupportedException) {
      status = Status.UNSUPPORTED_MEDIA_TYPE;
    } else if (exception instanceof InternalServerErrorException) {
      status = Status.INTERNAL_SERVER_ERROR;
    } else if (exception instanceof ServiceUnavailableException) {
      status = Status.SERVICE_UNAVAILABLE;
    } else {
      status = Status.INTERNAL_SERVER_ERROR;
      logger.debug("Unknown exception");
    }
    logger.trace(CommonDefinitions.END);
    return Response.status(status).entity(ReceiverDefinitions.COMMON_ERROR_CODE).build();
  }
}