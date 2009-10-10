/*
 * Copyright (c) 2008 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.io.IOException;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.DispatchEvents;
import com.jacob.com.Variant;

import vavi.util.Debug;
import vavix.util.ComUtil;


/**
 * iTunes COM API front endÅD
 * 
 * @target 1.1
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030211 nsano initial version <br>
 */
public class ITunesCom2 {

    public static class ITunesEvents {
        public void OnPlayerPlayEvent(Variant[] args) {
            System.out.println("OnPlayerPlayEvent");

            Dispatch event = args[0].getDispatch();

            System.out.println("Artist: " + Dispatch.get(event, "Artist"));
            System.out.println("Album: " + Dispatch.get(event, "Album"));
            System.out.println("Name: " + Dispatch.get(event, "Name"));
        }
    }

    /**
     * @throws IOException
     */
    public static void main(String[] args) throws Exception {

        ComThread.InitMTA(true);

        // manager
        ActiveXComponent component = new ActiveXComponent("iTunes.Application");
Debug.println("component: " + component);
        Dispatch itunes = component.getObject();
Debug.println("object: " + itunes);

        DispatchEvents events = new DispatchEvents(itunes, new ITunesEvents());
Debug.println("events: " + events);

    	Variant result = Dispatch.call(itunes, "LibraryPlaylist");
Debug.println("result: " + result.getvt() + ", " + ComUtil.toObject(result));

//        ComThread.Release();
    }
}

/* */
