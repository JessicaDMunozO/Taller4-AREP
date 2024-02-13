package edu.escuelaing.arem.ASE.app;

/**
 * Functional interface for a web service
 */
public interface WebService {

    /**
     * Handle web service request
     * 
     * @param param parameter of the query request
     * @return response of the web service
     */
    public String handle(String param);
}
