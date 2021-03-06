package org.FaraFan.Dao;

import org.FaraFan.Dao.Connection.EntityBase;
import org.FaraFan.DcuS.ReturnMessage;
import org.FaraFan.SsaaException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

/**
 * User: Behrooz Mohamadi <behrooz.mohamadi.66@gmail.com>
 * Date: 9/2/12
 * Time: 11:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class DcuServiceLogsRepository extends EntityBase {
    final ResourceBundle resource = ResourceBundle.getBundle("SsaaConfig");
    String databaseUserName = resource.getString("DatabaseUserName");
    public DcuServiceLogsRepository(Connection cn) {
        super(cn);
    }

    public void saveLogs(String Schemsa, ReturnMessage message, Long lastRequest, String NationalRegisterNo, Long evenInfoID) throws Exception {
        Logger.getLogger(DcuServiceLogsRepository.class).warn("Dcu Service Error:"+message.getCode()+" Message:"+message.getDescription());

        String query =  "INSERT INTO "+Schemsa+".DCUSERVICE_LOGS " +
                        "(Id,Code,Description,TraceCode,DB_COMMIT_DATE," +
                        "Request_ID,NATIONALREGISTERNO,EVENT_INFO_ID) values " +
                        "(" + databaseUserName + ".SEQ_TBL_DCUSERVICE_LOGS.NEXTVAL,?,?,?,SYSDATE,?,?,?)";
        try {
            PreparedStatement stmt = getConnection().prepareStatement(query);
            stmt.setInt(1, message.getCode());
            stmt.setString(2, message.getDescription());
            stmt.setLong(3, message.getTraceCode());
            stmt.setLong(4,lastRequest);
            stmt.setString(5, NationalRegisterNo);
            stmt.setLong(6, evenInfoID);
            stmt.executeUpdate();
        } catch (Exception ex) {
            throw new SsaaException(ex.getMessage());
        }
    }
}
