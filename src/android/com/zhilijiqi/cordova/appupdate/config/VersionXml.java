package com.zhilijiqi.cordova.appupdate.config;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

/**
 * Created by Feng on 2017/9/7.
 */

public class VersionXml {

    private final static String FORCE_UPDATE = "1";

    private String name;
    private String message;
    private int version;
    private String url;
    private boolean force;
    private int minNativeVersion;

    public VersionXml(String data){
        this.parseXMLWithPUll(data);
    }
    private void parseXMLWithPUll(String xmlData){
        try{

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));

            int eventType = xmlPullParser.getEventType();
            while(eventType!=XmlPullParser.END_DOCUMENT){
                String nodeName = xmlPullParser.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:{
                        if("name".equals(nodeName)){
                            name = xmlPullParser.nextText();
                        }else if("message".equals(nodeName)){
                            message = xmlPullParser.nextText();
                        }else if("version".equals(nodeName)){
                            version = Integer.parseInt(xmlPullParser.nextText());
                        }else if("url".equals(nodeName)){
                            url = xmlPullParser.nextText();
                        }else if("force".equals(nodeName)){
                            String value = xmlPullParser.nextText();
                            force = FORCE_UPDATE.equals(value);
                        }else if("min_native_version".equals(nodeName)){
                            String value = xmlPullParser.nextText();
                            minNativeVersion = Integer.parseInt(value);
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG:{
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean getForce(){
        return this.force;
    }
    public void setForce(boolean force){
        this.force = force;
    }

    public String getName() {
        return this.name;
    }

    public String getMessage() {
        return this.message;
    }

    public int getVersion() {
        return this.version;
    }

    public String getUrl() {
        return this.url;
    }

    public int getMinNativeVersion(){
        return this.minNativeVersion;
    }
}
