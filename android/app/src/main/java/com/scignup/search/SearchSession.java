package com.scignup.search;

import android.util.Log;
import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.document.html.HtmlDocument;
import com.gistlabs.mechanize.document.html.HtmlElement;
import com.gistlabs.mechanize.document.html.HtmlNode;
import com.gistlabs.mechanize.document.html.form.Form;
import com.gistlabs.mechanize.document.html.form.SubmitButton;
import com.gistlabs.mechanize.document.html.form.SubmitImage;
import com.gistlabs.mechanize.document.node.Node;
import com.gistlabs.mechanize.impl.MechanizeAgent;
import com.gistlabs.mechanize.parameters.Parameters;
import com.gistlabs.mechanize.requestor.RequestBuilder;
import com.gistlabs.mechanize.util.css.CSSHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by matt on 12/24/15.
 */
public class SearchSession {

    private static final String SEARCH_URL = "https://rice.usc.edu/kr-prd/kr/lookup.do" +
            "?methodToCall=start&businessObjectClassName=org.kuali.rice.kim.bo.Person&" +
            "docFormKey=88888888&returnLocation=https://rice.usc.edu/kr-prd/portal.do&" +
            "hideReturnLink=true";
    private static final String SSO_REDIRECT_URL = "https://shibboleth.usc.edu:443/idp/profile/" +
            "SAML2/Redirect/SSO";
    private static final String SSO_URL = "https://shibboleth.usc.edu:443/idp/Authn/UserPassword";

    private final MechanizeAgent agent;

    public SearchSession() {
        this.agent = new MechanizeAgent();
    }

    public void login(String username, String password) throws InvalidLoginException {
        HtmlDocument doc = agent.get(SEARCH_URL);
        Form login = doc.form("loginform");
        login.get("j_username").set(username);
        login.get("j_password").set(password);
        HtmlDocument resp = login.submit();
        if (resp.getUri().equals(SSO_REDIRECT_URL)) {
            Form redirect = resp.forms().get(0);
            redirect.submit();
        } else {
            throw new InvalidLoginException(username, password);
        }
    }

    public List<String[]> search(Map<String, String> data) throws Exception {
        HtmlDocument doc = agent.get(SEARCH_URL);
        if (doc.getUri().equals(SSO_URL)) {
            throw new IllegalStateException("Cannot perform search without authentication.");
        }
        Form search = doc.form("KualiForm");
        for (Map.Entry<String, String> entry : data.entrySet()) {
            search.get(entry.getKey()).set(entry.getValue());
        }
        Method formComposeParameters = Form.class.getDeclaredMethod("composeParameters",
                SubmitButton.class, SubmitImage.class, int.class, int.class);
        formComposeParameters.setAccessible(true);
        Parameters parameters = (Parameters) formComposeParameters.invoke(search, null, null, 0, 0);
        parameters.add("methodToCall.search", "search");
        RequestBuilder<Resource> request = agent.doRequest(search.getUri()).set(parameters);
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        HtmlDocument resp = request.post();
        HtmlElement table = resp.find(CSSHelper.byIdOrName("row"));
        HtmlElement header = table.find("thead");
        List<? extends Node> headerCols = header.findAll("th");
        String[] headerRow = new String[headerCols.size()];
        for (int i = 0; i < headerCols.size(); ++i) {
            headerRow[i] = headerCols.get(i).getValue();
        }
        List<String[]> ret = new ArrayList<>();
        ret.add(headerRow);
        List<? extends Node> rows = table.find("tbody").findAll("tr");
        for (int i = 0; i < rows.size(); ++i) {
            List<? extends Node> rowCols = rows.get(i).findAll("td");
            String[] row = new String[rowCols.size()];
            for (int j = 0; j < rowCols.size(); ++j) {
                row[j] = rowCols.get(j).getValue();
            }
            ret.add(row);
        }
        return ret;
    }

}
