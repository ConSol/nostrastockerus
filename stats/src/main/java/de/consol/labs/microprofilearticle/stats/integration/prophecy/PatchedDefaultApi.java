package de.consol.labs.microprofilearticle.stats.integration.prophecy;

import de.consol.labs.microprofilearticle.prophecy.generatedclient.ApiClient;
import de.consol.labs.microprofilearticle.prophecy.generatedclient.ApiException;
import de.consol.labs.microprofilearticle.prophecy.generatedclient.ApiResponse;
import de.consol.labs.microprofilearticle.prophecy.generatedclient.Pair;
import de.consol.labs.microprofilearticle.prophecy.generatedclient.api.DefaultApi;
import de.consol.labs.microprofilearticle.prophecy.generatedclient.model.Prophecy;

import javax.ws.rs.core.GenericType;
import java.util.*;

public class PatchedDefaultApi extends DefaultApi {

    public PatchedDefaultApi(final ApiClient apiClient) {
        super(apiClient);
    }

    @Override
    public ApiResponse<List<Prophecy>> getPropheciesCreatedByUserWithHttpInfo(final String userName) throws ApiException {
        final Object localVarPostBody = new Object();
        // verify the required parameter 'userName' is set
        if (userName == null) {
            throw new ApiException(400, "Missing the required parameter 'userName' when calling getPropheciesCreatedByUser");
        }
        // create path and map variables
        final String localVarPath = "/prophecy/createdBy/{userName}"
                .replaceAll("\\{" + "userName" + "\\}", getApiClient().escapeString(userName.toString()));
        // query params
        final List<Pair> localVarQueryParams = new ArrayList<Pair>();
        final Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        final Map<String, Object> localVarFormParams = new HashMap<String, Object>();
        final String[] localVarAccepts = {
                "application/json"
        };
        final String localVarAccept = getApiClient().selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {};
        final String localVarContentType = getApiClient().selectHeaderContentType(localVarContentTypes);
        final String[] localVarAuthNames = new String[]{"jwt"};
        final GenericType<Prophecy[]> localVarReturnType = new GenericType<Prophecy[]>() {
        };
        // fixes the {@link java.lang.ClassCastException} at runtime
        // by using array instead of generic list.
        final ApiResponse<Prophecy[]> result = getApiClient().invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
        final List<Prophecy> repackagedData = new ArrayList<>(Arrays.asList(result.getData()));
        return new ApiResponse<>(result.getStatusCode(), result.getHeaders(), repackagedData);
    }
}
