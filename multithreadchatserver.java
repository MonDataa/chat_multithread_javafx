package org.example.blocking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class multithreadchatserver extends Thread{
    private List<Conversation> conversationList=new ArrayList<>();
    int clientcount;
    public static void main( String[] args )
    {
        new multithreadchatserver().start();

    }
    @Override
    public void run(){
        System.out.println("the server is started using port <1234>");
        try {
            ServerSocket serversocket = new ServerSocket(1234);
            while (true){
                Socket socket = serversocket.accept();
                ++clientcount;
                Conversation conversation = new Conversation(socket,clientcount);
                conversationList.add(conversation);
                conversation.start();
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
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);
                /*pw.println("response");
                pw.flush(); dans le cas de false*/
                String ip = socket.getRemoteSocketAddress().toString();
                System.out.println("new client conn => " + clientid + " ip : " + ip);
                pw.println("welcome your id is : " + clientid);
                String request;
                while ((request = br.readLine()) != null) {
                    //String request=br.readLine();
                    System.out.println("new request => ip =" + ip + "request=" + request);
                    List<Integer> clientsto = new ArrayList<>();
                    String message;
                    if (request.contains("=>")) {
                        String[] items = request.split("=>");
                        String clients = items[0];
                         message = items[1];
                        if (clients.contains(",")) {
                            String[] clientid = clients.split(",");
                            for (String id : clientid) {
                                clientsto.add(Integer.parseInt(id));
                            }
                            //String client=clientid[0];
                        } else {
                            clientsto.add(Integer.parseInt(clients));
                        }
                    }
                    else {
                        clientsto=conversationList.stream().map(c->c.clientid).collect(Collectors.toList());
                        message=request;
                    }
                    broadcastMessage(message,this,clientsto);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
    public void broadcastMessage(String message,Conversation from,List<Integer> clients) throws IOException {

            try {
                for(Conversation conversation:conversationList) {
                    if(conversation!=from && clients.contains(conversation.clientid)){
                        Socket socket = conversation.socket;
                        OutputStream outputStream = socket.getOutputStream();
                        PrintWriter printWriter = new PrintWriter(outputStream, true);
                        printWriter.println(message);
                    }

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

