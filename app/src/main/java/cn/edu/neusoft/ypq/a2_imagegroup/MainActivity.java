package cn.edu.neusoft.ypq.a2_imagegroup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    // 用户名和密码
    public static final String NAME = "yanpeiqi";
    public static final String PWD = "123456";
    public static final String KEY_NAME = "name";
    public static final String KEY_PWD = "123456";
    public static final String PREFERENCE_NAME = "login_info";


    // 登陆检测、视图绑定和点击事件的触发
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkInfo();

        EditText etName = findViewById(R.id.et_name);
        EditText etPwd = findViewById(R.id.et_pwd);
        Button button = findViewById(R.id.bt_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etName.getText().toString().equals(NAME) && etPwd.getText().toString().equals(PWD)) {
                    saveInfo();
                    toListActivity();
                } else {
                    Toast.makeText(MainActivity.this,"用户名或密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 跳转到列表界面，结束登陆界面
    public void toListActivity() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ListActivity.class);
        startActivity(intent);
        Toast.makeText(MainActivity.this , "登录成功" , Toast.LENGTH_SHORT).show();
        finish();
    }

    // 自动登录的检测
    public void checkInfo() {
        // Context.MODE_PRIVATE : 私有文件，只有应用本身能访问
        SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME , Context.MODE_PRIVATE);
        String name = preferences.getString(KEY_NAME, "");
        String pwd = preferences.getString(KEY_PWD, "");
        if (name.equals(NAME) && pwd.equals(PWD)) {
            toListActivity();
        }
    }

    // 登陆成功时数据保存
    public void saveInfo() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_NAME , NAME);
        editor.putString(KEY_PWD , PWD);
        editor.commit();
    }
}