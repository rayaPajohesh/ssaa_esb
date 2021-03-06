package org.FaraFan;

import org.FaraFan.Common.Utils;
import org.FaraFan.Dao.Connection.DataAccess;
import org.FaraFan.Dao.EventInfoRepository;
import org.FaraFan.Dao.RequestRepository;
import org.FaraFan.Entity.Responce.response;
import org.FaraFan.Entity.Result.EventPacket;
import org.FaraFan.EsbService.ServiceLocator;
import org.FaraFan.EsbService.ServiceSoap;
import org.apache.log4j.Logger;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ResourceBundle;

/**
 * Created by Behrooz on 04/21/2015.
 */
public class SsaaLocal {
    public static void main(String[] argv) throws Exception {
        Connection cn = null;
        try {
            final ResourceBundle resource = ResourceBundle.getBundle("SsaaConfig");
            int eventPacketCount = Integer.parseInt(resource.getString("EventPacketCount"));
            int delayTime = Integer.parseInt(resource.getString("DelayTime"));
            String Schema = resource.getString("Schema");
            System.out.println("--->Ssaa Version: " + Utils.getCurrentVersion());
            int count = 0;

            while (true) {
                count += 1;
                if ((cn == null) || (cn.isClosed()))
                    cn = DataAccess.getConnection();

//                if (!Utils.ServiceAvailable(resource.getString("Ssaa_WSDL_URL")))
//                    throw new SsaaException("Ssaa Service not available!");

//                Long lastRequest = new RequestRepository(cn).GetLastRequest(Schema);
             Long lastRequest = Long.valueOf(count);
                String requestData = Utils.parse_object_to_xml(resource,++lastRequest,eventPacketCount);
                System.out.println(requestData);

                ServiceSoap binding;
                binding = (ServiceSoap) new ServiceLocator().getServiceSoap(new URL(resource.getString("Ssaa_WSDL_URL")));
//                String responseData = binding.xmsRequest(requestData);
//                System.out.println(responseData);
              String responceDataFromFile=new String (Files.readAllBytes(Paths.get("D:\\Farafan\\SSAA\\XML4(change)\\" + count +".txt")),"UTF-8");
              System.out.println(responceDataFromFile);
                response response = Utils.parse_xml_to_object(responceDataFromFile);

                if (response == null) {
                    Logger.getLogger(Ssaa.class).warn("Ssaa Service response is null!");
                    System.out.println("--->Ssaa Service response is null!");
                    System.out.println("--->Waiting for "+delayTime+" ms...");
                    Thread.sleep(delayTime);
                    continue;
                }
                if (!response.getCode().equals("ok")) {
                    Logger.getLogger(Ssaa.class).warn("Ssaa Service Responce.Message :"+response.getCode());
                    System.out.println("--->Ssaa Service Responce.Message :" + response.getCode());
                    System.out.println("--->Waiting for "+delayTime+" ms...");
                    Thread.sleep(delayTime);
                    continue;
                }
                if (response.getBody() == null) {
                    Logger.getLogger(Ssaa.class).warn("Ssaa Service response body is null!");
                    System.out.println("--->Ssaa Service response is null!");
                    System.out.println("--->Waiting for "+delayTime+" ms...");
                    Thread.sleep(delayTime);
                    continue;
                }
                if (!response.getBody().getResult().getErrorCode().equals("0")) {
                    Logger.getLogger(Ssaa.class).warn("Ssaa Service Result.ErrorCode:" + response.getBody().getResult().getErrorCode() + " ,Result.ErrorMessage:" + response.getBody().getResult().getErrorMessage());
                    System.out.println("--->Ssaa Service Result.ErrorCode:" + response.getBody().getResult().getErrorCode() + " ,Result.ErrorMessage:" + response.getBody().getResult().getErrorMessage());
                    System.out.println("--->Waiting for "+delayTime+" ms...");
                    Thread.sleep(delayTime);
                    continue;
                }
                if (response.getBody().getResult().getBody().getEventPacket().size() == 0) {
                    Logger.getLogger(Ssaa.class).warn("Ssaa Service has no any Result.eventPacket!");
                    System.out.println("--->Ssaa Service has no any Result.eventPacket!");
                    System.out.println("--->Waiting for "+delayTime+" ms...");
                    Thread.sleep(delayTime);
                    continue;
                }
                eventPacketCount=response.getBody().getResult().getBody().getEventPacket().size();
                new RequestRepository(cn).saveLastRequest(Schema, lastRequest, eventPacketCount);

                for (EventPacket eventPacket : response.getBody().getResult().getBody().getEventPacket()) {
                    if ((cn == null) || (cn.isClosed()))  cn = DataAccess.getConnection();
                    Long evenInfoID = new EventInfoRepository(cn).saveAll(eventPacket, lastRequest, Schema);
                    cn.commit();
                    Utils.call_DcuService(eventPacket, lastRequest, resource, evenInfoID);
                }

                DataAccess.dispose(cn);
                System.out.println("--->Successfully Request:" + lastRequest + " with "+ eventPacketCount +" EventPacketCount.");
            }
        }catch (Exception e){
            e.printStackTrace();
            cn.rollback();
            Logger.getLogger(Ssaa.class).error(e);

        }
        finally {
            DataAccess.dispose(cn);
            System.out.println("ok");
        }
    }
}
