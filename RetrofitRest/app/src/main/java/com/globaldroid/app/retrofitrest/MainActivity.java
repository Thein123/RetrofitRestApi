package com.globaldroid.app.retrofitrest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import Model.MultipleResource;
import Model.User;
import Model.UserList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    TextView responseText,data;
    CallInterface callInterface;
    TableLayout inflate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        responseText = (TextView) findViewById(R.id.responseText);
        //data=(TextView)findViewById(R.id.data);

        inflate=(TableLayout)findViewById(R.id.mytable);




        callInterface = APICall.getClient().create(CallInterface.class);

        /**
         GET List Resources
         **/
        Call<MultipleResource> call = callInterface.doGetListResources();

        call.enqueue(new Callback<MultipleResource>() {
            @Override
            public void onResponse(Call<MultipleResource> call, Response<MultipleResource> response) {


                Log.d("TAG", response.code() + "");

                String displayResponse = "";

                MultipleResource resource = response.body();
                Integer text = resource.page;
                Integer total = resource.total;
                Integer totalPages = resource.totalPages;
                List<MultipleResource.Datum> datumList = resource.data;

                displayResponse += text + " Page\n" + total + " Total\n" + totalPages + " Total Pages\n";

                for (MultipleResource.Datum datum : datumList) {
                    displayResponse += datum.id + " " + datum.name + " " + datum.pantoneValue + " " + datum.year + "\n";
                }

                responseText.setText(displayResponse);

            }

            @Override
            public void onFailure(Call<MultipleResource> call, Throwable t) {

                call.cancel();

            }
        });


        User user = new User("morpheus", "leader");
        Call<User> call1 = callInterface.createUser(user);
        call1.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user1 = response.body();

                Toast.makeText(getApplicationContext(), user1.name + " " + user1.job + " " + user1.id + " " + user1.createdAt, Toast.LENGTH_SHORT).show();

                  responseText.append("\n" + user1.name + " " + user1.job + " " + user1.id + " " + user1.createdAt);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                call.cancel();
            }
        });


        /**
         GET List Users
         **/
        Call<UserList> call2 = callInterface.doGetUserList("2");
        call2.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {

                UserList userList = response.body();
                Integer text = userList.page;
                Integer total = userList.total;
                Integer totalPages = userList.totalPages;
                List<UserList.Datum> datumList = userList.data;
                Toast.makeText(getApplicationContext(), text + " page\n" + total + " total\n" + totalPages + " totalPages\n", Toast.LENGTH_SHORT).show();


                for (UserList.Datum datum : datumList) {
                  //  data.append("\n"+"id : " + datum.id + " name: " + datum.first_name + " " + datum.last_name + " avatar: " + datum.avatar);
                          createTable(Integer.toString(datum.id),datum.first_name+" "+datum.last_name,datum.avatar);

                    Toast.makeText(getApplicationContext(), "id : " + datum.id + "  name: " + datum.first_name + " " + datum.last_name + "  avatar: " + datum.avatar, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                call.cancel();
            }
        });

        /**
         POST name and job Url encoded.
         **/
        Call<UserList> call3 = callInterface.doCreateUserWithField("morpheus","leader");
        call3.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                UserList userList = response.body();
                Integer text = userList.page;
                Integer total = userList.total;
                Integer totalPages = userList.totalPages;
                List<UserList.Datum> datumList = userList.data;
                Toast.makeText(getApplicationContext(), text + " page\n" + total + " total\n" + totalPages + " totalPages\n", Toast.LENGTH_SHORT).show();


                for (UserList.Datum datum : datumList) {
                   // data.append("\n"+"id : " + datum.id + " name: " + datum.first_name + " " + datum.last_name + " avatar: " + datum.avatar);
                    createTable(Integer.toString(datum.id),datum.first_name+" "+datum.last_name,datum.avatar);
                    Toast.makeText(getApplicationContext(), "id : " + datum.id + "  name: " + datum.first_name + " " + datum.last_name + "  avatar: " + datum.avatar, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                call.cancel();
            }
        });

    }


    public void createTable(String txt,String txt1,String imgurl){

        TableRow row=new TableRow(this);


        TextView idtext=new TextView(this);
         idtext.setText(txt);

        TextView  name=new TextView(this);

        name.setText(txt1);

        ImageView image=new ImageView(this);
        image.setPadding(5,5,5,5);
        Picasso.with(this)
                .load(imgurl)
                .resize(90,60)
                .into(image);

        row.addView(idtext);
        row.addView(name);
        row.addView(image);

        inflate.addView(row);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
