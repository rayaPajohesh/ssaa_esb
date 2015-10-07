/**
 * ServicesServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.FaraFan.DcuS;

public class ServicesServiceLocator extends org.apache.axis.client.Service implements ServicesService {

    public ServicesServiceLocator() {
    }


    public ServicesServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ServicesServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for Services
    private String Services_address = "http://10.7.17.86:7001/DcuS/services/Services";

    public String getServicesAddress() {
        return Services_address;
    }

    // The WSDD service name defaults to the port name.
    private String ServicesWSDDServiceName = "Services";

    public String getServicesWSDDServiceName() {
        return ServicesWSDDServiceName;
    }

    public void setServicesWSDDServiceName(String name) {
        ServicesWSDDServiceName = name;
    }

    public Services getServices() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Services_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getServices(endpoint);
    }

    public Services getServices(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ServicesSoapBindingStub _stub = new ServicesSoapBindingStub(portAddress, this);
            _stub.setPortName(getServicesWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setServicesEndpointAddress(String address) {
        Services_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (Services.class.isAssignableFrom(serviceEndpointInterface)) {
                ServicesSoapBindingStub _stub = new ServicesSoapBindingStub(new java.net.URL(Services_address), this);
                _stub.setPortName(getServicesWSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("Services".equals(inputPortName)) {
            return getServices();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://Server", "ServicesService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://Server", "Services"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("Services".equals(portName)) {
            setServicesEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
