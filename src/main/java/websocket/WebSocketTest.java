package websocket;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
/*
 * @ServiceEndpoint注解是一个类层次的注解，它的功能是将目前的类定义成一个websocket服务器
 * 
 *
 */
@ServerEndpoint("/websocket")
public class WebSocketTest {
	//静态变量，用来记录当前在前连接数。设计为线程安全的
	private static int onlineCount = 0;
	
	//concurrent包的线程安全set，用来存放每个客户端对象的MyWebSocket对象。若要实现服务端与单一客户端通信，可以使用map存放，其中key作为用户标识
	private static CopyOnWriteArraySet<WebSocketTest> webSocket = new CopyOnWriteArraySet<WebSocketTest>();
	
	//与某个客户端的连接会话，需要通过它来个客户端发送数据
	private Session session;
	
	/*
	 * 连接建立成功调用的方法
	 * @Param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	@OnOpen
	public void onOpen(Session session){
		this.session = session;
		webSocket.add(this);	//加入set中
		addOnlineCount();		//在线数加1
		System.out.println("有新连接加入，当前人数为"+getOnLineCount());
	}
	/*
	 * 连接关闭的方法
	 */
	@OnClose
	public void onClose(Session session){
		webSocket.remove(this);	//从set中删除
		subOnLineCount();		//在线数减一
		System.out.println("有一连接关闭，当前在线人数为"+getOnLineCount());
	}
	/*
	 * 收到客户端消息后调用的方法
	 * @Param message 客户端发送过来的消息
	 * @Param session 可选的参数
	 */
	@OnMessage
	public void onMessage(String message,Session session){
		System.out.println("来自客户端的消息"+message);
		//群发消息
		for(WebSocketTest item :webSocket){
			try {
				item.sendMessage(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/*
	 * 发生错误时调用
	 * @Param session
	 * @Param error
	 */
	public void onError(Session session,Throwable error){
		System.out.println("发生错误");
		error.printStackTrace();
	}
	/*
	 * 自定义的方法
	 */
	private void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}
	private static synchronized void subOnLineCount() {
		WebSocketTest.onlineCount--;
	}
	private static synchronized int getOnLineCount() {
		return onlineCount;
	}

	private static synchronized void addOnlineCount() {
		WebSocketTest.onlineCount++;
	}
}
