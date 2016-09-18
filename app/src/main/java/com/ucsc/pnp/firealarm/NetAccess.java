package com.ucsc.pnp.firealarm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by nrv on 7/19/16.
 */
public class NetAccess extends Service {
    private static final String TAG = NetAccess.class.getName();

    private static final String Data_HOST = "192.168.1.2";
    private static final int Data_PORT = 9090;

    // we are listing for UDP socket
    private DatagramSocket socket;

    // server address
    private InetAddress address;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        initUdpSocket();
        initUdpSender();
        initUdpListener();
        return START_STICKY;
    }


    private void initUdpSocket() {
        if (socket == null || socket.isClosed()) {
            try {
                socket = new DatagramSocket();
            } catch (SocketException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "Socket already initialized");
        }
    }

    private void initUdpSender() {
        if (socket != null) {
            new Thread(new Runnable() {
                public void run() {
                    //
                    // sendPingMessage();
                    String message="InitUDP";
                    if (address == null) try {
                        address = InetAddress.getByName(Data_HOST);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    DatagramPacket sendPacket = new DatagramPacket(message.getBytes(), message.length(), address, Data_PORT);
                    try {
                        socket.send(sendPacket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            Log.e(TAG, "Socket not connected");
        }
    }

    private void initUdpListener() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    byte[] message = new byte[512];

                    while (true) {
                        // listen for data
                        DatagramPacket receivePacket = new DatagramPacket(message, message.length);
                        socket.receive(receivePacket);
                        String emssage = new String(message, 0, receivePacket.getLength());

                        Log.e(TAG, "msg received: " + emssage);


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



}
