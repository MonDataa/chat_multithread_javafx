package org.example.blocking;

import sun.plugin2.message.Conversation;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class multithreadblockingserver extends Thread{
    int clientcount;
    public static void main( String[] args )
    {
        new multithreadblockingserver().start();

    }
    @Override
    public void run(){
        System.out.println("the server is started using port <1234>");
        try {
            ServerSocket serversocket = new ServerSocket(1234);
            while (true){
                Socket socket = serversocket.accept();
                ++clientcount;
                new Conversation(socket,clientcount).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    class Conversation extends Thread{
        private Socket socket;
        private int clientid;
        public Conversation(Socket socket,int clientid){
            this.socket=socket;
            this.clientid=clientid;
        }
        @Override
        public void run(){
            try {
                InputStream is= socket.getInputStream();
                InputStreamReader isr=new InputStreamReader(is);
                BufferedReader br=new BufferedReader(isr);

                OutputStream os=socket.getOutputStream();
                PrintWriter  pw=new PrintWriter(os,true);
                /*pw.println("response");
                pw.flush(); dans le cas de false*/
                String ip=socket.getRemoteSocketAddress().toString();
                System.out.println("new client conn => "+clientid+" ip : "+ip);
                pw.println("welcome your id is : "+clientid);
                String request;
                while ((request=br.readLine())!=null){
                    //String request=br.readLine();
                    System.out.println("new request => ip ="+ip+"request="+request);
                    String response="size ="+request.length();
                    pw.println(response);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
