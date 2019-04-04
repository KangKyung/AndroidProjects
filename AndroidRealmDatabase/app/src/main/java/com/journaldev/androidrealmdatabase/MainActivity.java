package com.journaldev.androidrealmdatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnAdd;
    EditText inName, inAge; //  입력
    TextView textView;  //  출력
    Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(getApplicationContext());

        RealmConfiguration config =
                new RealmConfiguration.Builder()
                        .name("chatttRecord.db")
                        .deleteRealmIfMigrationNeeded()
                        .build();

        Realm.setDefaultConfiguration(config);

        initViews();

        mRealm = Realm.getDefaultInstance();

        // 화면에 출력
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                RealmResults<Employee> results = realm.where(Employee.class).findAll();
                textView.setText("");
                for (Employee employee : results) {
                    textView.append(employee.name + " : " + employee.age + "\n");
                }
            }
        });
    }

    private void initViews() {
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        textView = findViewById(R.id.textViewEmployees);

        inName = findViewById(R.id.inName);
        inAge = findViewById(R.id.inAge);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnAdd:
                addEmployee();
                break;
        }
    }

    private void addEmployee() {

        Realm realm = null;

        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                if (!inName.getText().toString().trim().isEmpty()) {
                    Employee employee = new Employee();
                    employee.name = inName.getText().toString().trim();

                    if (!inAge.getText().toString().trim().isEmpty())
                        employee.age = Integer.parseInt(inAge.getText().toString().trim());

                    realm.copyToRealm(employee);
                }
            }
        });

        if (realm != null) {
            realm.close();
        }

        // 화면에 출력
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                RealmResults<Employee> results = realm.where(Employee.class).findAll();
                textView.setText("");
                for (Employee employee : results) {
                    textView.append(employee.name + " : " + employee.age + "\n");
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRealm != null) {
            mRealm.close();
        }
    }
}
