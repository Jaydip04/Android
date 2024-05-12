package com.jaydip.crudroom;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.jaydip.crudroom.database.RoomDB;
import com.jaydip.crudroom.model.Person;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context context;

    private EditText edtName,edtAge,edtId;
    private Button btnAdd,btnUpdate,btnDelete,btnShow;

    private List<Person> personList;
    RoomDB roomDB;

    private void init(){
        context = MainActivity.this;
        edtName = findViewById(R.id.name);
        edtAge = findViewById(R.id.age);
        edtId = findViewById(R.id.id);

        btnAdd = findViewById(R.id.add);
        btnUpdate = findViewById(R.id.update);
        btnDelete = findViewById(R.id.delete);
        btnShow = findViewById(R.id.show);

        roomDB = RoomDB.getInstance(context);
        personList = roomDB.mainDAO().getALL();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        getWindow().setStatusBarColor(ContextCompat.getColor(context,R.color.white));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtName.getText().length() == 0 || edtAge.getText().length() == 0){
                    showToast("fill name and age...");
                }else{
                    Person newPerson = new Person();
                    newPerson.setName(edtName.getText().toString());
                    newPerson.setAge(Integer.parseInt(edtAge.getText().toString()));
                    roomDB.mainDAO().insert(newPerson);
                    listUpdate();
                    showToast("Add Successfully...");
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtId.getText().length() == 0){
                    showToast("fill id...");
                }else{
                    int deleteID = Integer.parseInt(edtId.getText().toString());
                    Person deletedPerson = null;
                    for (Person p : personList){
                        if (p.getId() == deleteID){
                            deletedPerson = p;
                            break;
                        }
                    }
                    if (deletedPerson  == null){
                        showToast("Person not that");
                    }else{
                        roomDB.mainDAO().delete(deletedPerson);
                        listUpdate();
                        showToast("Deleted Successfully...");
                    }
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtName.getText().length() == 0 || edtAge.getText().length() == 0){
                    showToast("fill name and age...");
                }else{
                    int updateId = Integer.parseInt(edtId.getText().toString());
                    boolean isThere = false;
                    for (Person p : personList){
                        if (p.getId() == updateId){
                            isThere = true;
                            break;
                        }
                    }
                    if (isThere){
                        String updateName = edtName.getText().toString();
                        String updateAge = edtAge.getText().toString();
                        roomDB.mainDAO().update(updateId,updateName,Integer.parseInt(updateAge));
                        listUpdate();
                        showToast("Update Successfully...");
                    }else{
                        showToast("Id not in...");
                    }
                }
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder stringBuilder = new StringBuilder();
                for(Person person : personList){
                    stringBuilder.append(person.getName()+"  "+person.getAge()+"  "+person.getId());
                    stringBuilder.append("\n");
                }
                String alldata = stringBuilder.toString();
                showToast(alldata);
            }
        });

    }
    private void listUpdate(){
        personList.clear();
        personList.addAll(roomDB.mainDAO().getALL());
    }
    private void showToast(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}