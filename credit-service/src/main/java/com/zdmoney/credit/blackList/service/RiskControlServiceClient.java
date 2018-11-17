package com.zdmoney.credit.blackList.service;



import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.aegis.AegisBindingProvider;
import org.codehaus.xfire.annotations.AnnotationServiceFactory;
import org.codehaus.xfire.annotations.jsr181.Jsr181WebAnnotations;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.jaxb2.JaxbTypeRegistry;
import org.codehaus.xfire.service.Endpoint;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.soap.AbstractSoapBinding;
import org.codehaus.xfire.transport.TransportManager;

import com.zdmoney.credit.blackList.service.pub.RiskControlServiceSoap;

import javax.xml.namespace.QName;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;

public class RiskControlServiceClient {

    private static XFireProxyFactory proxyFactory = new XFireProxyFactory();
    private HashMap endpoints = new HashMap();
    private Service service0;

    public RiskControlServiceClient() {
        create0();
        Endpoint RiskControlServiceSoapLocalEndpointEP = service0 .addEndpoint(new QName("http://www.zendai.com/", "RiskControlServiceSoapLocalEndpoint"), new QName("http://www.zendai.com/", "RiskControlServiceSoapLocalBinding"), "xfire.local://RiskControlService");
        endpoints.put(new QName("http://www.zendai.com/", "RiskControlServiceSoapLocalEndpoint"), RiskControlServiceSoapLocalEndpointEP);
        Endpoint RiskControlServiceSoapEP = service0 .addEndpoint(new QName("http://www.zendai.com/", "RiskControlServiceSoap"), new QName("http://www.zendai.com/", "RiskControlServiceSoap"), "http://114.80.110.32:8119/RiskControlService.asmx");
        endpoints.put(new QName("http://www.zendai.com/", "RiskControlServiceSoap"), RiskControlServiceSoapEP);
    }

    public Object getEndpoint(Endpoint endpoint) {
        try {
            return proxyFactory.create((endpoint).getBinding(), (endpoint).getUrl());
        } catch (MalformedURLException e) {
            throw new XFireRuntimeException("Invalid URL", e);
        }
    }

    public Object getEndpoint(QName name) {
        Endpoint endpoint = ((Endpoint) endpoints.get((name)));
        if ((endpoint) == null) {
            throw new IllegalStateException("No such endpoint!");
        }
        return getEndpoint((endpoint));
    }

    public Collection getEndpoints() {
        return endpoints.values();
    }

    private void create0() {
        TransportManager tm = (org.codehaus.xfire.XFireFactory.newInstance().getXFire().getTransportManager());
        HashMap props = new HashMap();
        props.put("annotations.allow.interface", true);
        AnnotationServiceFactory asf = new AnnotationServiceFactory(new Jsr181WebAnnotations(), tm, new AegisBindingProvider(new JaxbTypeRegistry()));
        asf.setBindingCreationEnabled(false);
        service0 = asf.create((RiskControlServiceSoap.class), props);
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://www.zendai.com/", "RiskControlServiceSoapLocalBinding"), "urn:xfire:transport:local");
        }
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://www.zendai.com/", "RiskControlServiceSoap"), "http://schemas.xmlsoap.org/soap/http");
        }
    }

    public RiskControlServiceSoap getRiskControlServiceSoapLocalEndpoint() {
        return ((RiskControlServiceSoap)(this).getEndpoint(new QName("http://www.zendai.com/", "RiskControlServiceSoapLocalEndpoint")));
    }

    public RiskControlServiceSoap getRiskControlServiceSoapLocalEndpoint(String url) {
        RiskControlServiceSoap var = getRiskControlServiceSoapLocalEndpoint();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public RiskControlServiceSoap getRiskControlServiceSoap() {
        return ((RiskControlServiceSoap)(this).getEndpoint(new QName("http://www.zendai.com/", "RiskControlServiceSoap")));
    }

    public RiskControlServiceSoap getRiskControlServiceSoap(String url) {
        RiskControlServiceSoap var = getRiskControlServiceSoap();

        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

}
