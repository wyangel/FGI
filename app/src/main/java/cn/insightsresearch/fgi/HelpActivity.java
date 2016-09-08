package cn.insightsresearch.fgi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {
    private TextView mTitle;
    private TextView textView;
    private String htmlStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.list_about);

        textView = (TextView)findViewById(R.id.textViewHelp);
        htmlStr = "<html><head><title><h3><b>导知FGI客户端(Ver1.0)</b></h3></title></head><body><p><h4>Copyright (c)  2016 InsightsResearch</h4></p>" +
                "<h5><p>" +
                "导知作为一家专业化市场调查研究公司，其覆盖了全国的优势资源，在全国大多数大中小城市均有办事机构或合作单位。 " +
                "自2002年创建以来，导知总以不断努力、进取的姿态，在中国市场累积了丰富的调查实绩并开创了多种专业的调查技术。" +
                "导知始终坚持在保证高品质服务的同时、积极地推进本地化以及创新性，以求为客户提供更为快速且高成效的市场调查服务。</p>" +
                "<p>导知的前身是日本新秦在中国本土的市场调研公司，自2002年创建以来，为众多日系、欧美系以及中国本土企业提供过市场调查服务。" +
                "2015年，企业内部调整的同时将公司名也由「Searchina」改成了「Insights research」，" +
                "寓意着导知将坚持以当地视角着眼于中国市场的动态，更深层次、更迅速地掌握消费者的嗜好，为客户提供物超所值的消费者洞察服务 。</p>" +
                "<p>请在授权下使用，谢谢配合！</p>" +
                "<p>导知（上海）信息咨询有限公司<br />insights(ShangHai) Co., Ltd. </p>" +
                "<p>公司地址：<br />上海市虹口区东大名路558号新华保险大楼202室　200120</p>" +
                "<p>联络方式：<br />Tel：+86-21-33663278<br />Fax：+86-21-33663276</p></h5></body></html>";

        textView.setText(Html.fromHtml(htmlStr));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        overridePendingTransition(R.anim.hold, android.R.anim.fade_out);
    }

}
