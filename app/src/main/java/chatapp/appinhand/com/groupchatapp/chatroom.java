package chatapp.appinhand.com.groupchatapp;

import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
//import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Superoft on 8/3/2016.
 */
public class chatroom extends ActionBarActivity {

    EditText chatmsg;
    Button send;
TextView allMsgs;
String username,roomname;
String chat_img;
String random_key;
    String bitmapstring;
    private DatabaseReference root;
    private  String chat_msg,chat_username;
ImageView img1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.tree);
       bitmapstring=  BitMapToString(largeIcon);



        Log.i("bimap",bitmapstring.toString());



        img1= (ImageView) findViewById(R.id.img1);

        //Log.i("returnbitmpa",bitmapstring);

     //   Bitmap strtobitmap=  StringToBitMap(bitmapstring);
       // img1.setImageBitmap(strtobitmap);


        chatmsg= (EditText) findViewById(R.id.typemsg);
        send= (Button) findViewById(R.id.send);
        allMsgs= (TextView) findViewById(R.id.chatmsgs);
username=getIntent().getExtras().get("username").toString();

        roomname=getIntent().getExtras().get("roomname").toString();

       // setTitle("Room name: "+roomname);
        final DatabaseReference root= FirebaseDatabase.getInstance().getReference().child(roomname);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map1=new HashMap<String, Object>();
               random_key= root.push().getKey();
root.updateChildren(map1);

DatabaseReference mesaageroot=root.child(random_key);
                HashMap<String,Object> map2=new HashMap<String, Object>();
              map2.put("name",username.toString());
                map2.put("msg", chatmsg.getText().toString());
map2.put("img",bitmapstring.toString());
                mesaageroot.updateChildren(map2);

            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat_conversion(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat_conversion(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private void append_chat_conversion(DataSnapshot dataSnapshot)
    {

//        Set<String> set=new HashSet<String>();
        Iterator i =dataSnapshot.getChildren().iterator();

        while ((i.hasNext()))
        {
            chat_img=(String)(((DataSnapshot) i.next()).getValue());
            chat_msg=(String)(((DataSnapshot) i.next()).getValue());
            chat_username=(String)(((DataSnapshot) i.next()).getValue());


            Log.i("returnbitmpa",chat_img);
          Bitmap strtobitmap=  StringToBitMap(chat_img);
            img1.setImageBitmap(strtobitmap);
           // Notification.Builder.setLargeIcon(strtobitmap);
        }
       allMsgs.append(chat_username+"\n"+chat_msg+"\n");

//        Log.i("returnbitmpa",bitmapstring);
//
//        Bitmap strtobitmap=  StringToBitMap(bitmapstring);
//                img1.setImageBitmap(strtobitmap);

        // of the final line and then subtracting the TextView's height
//        final int scrollAmount = allMsgs.getLayout().getLineTop(allMsgs.getLineCount()) - allMsgs.getHeight();
//        // if there is no need to scroll, scrollAmount will be <=0
//        if (scrollAmount > 0)
//            allMsgs.scrollTo(0, scrollAmount);
//        else
//            allMsgs.scrollTo(0, 0);
    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
