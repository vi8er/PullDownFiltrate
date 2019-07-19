package com.ys.pulldownfiltrate

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ys.pulldownfiltrate.PullDownFiltrate.BaseQuickPullDownFIlrateItem
import com.ys.pulldownfiltrate.PullDownFiltrate.PullDownFiltrateView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var pullDown: PullDownFiltrateView? = null
    var tvCountry: TextView? = null
    var tvMoney: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pullDown = findViewById(R.id.pulldown)
        tvCountry = findViewById(R.id.tv_country)
        tvMoney = findViewById(R.id.tv_money)

        val countryAdapter = DemoAdapter(this)
        countryAdapter.data = getData(0)
        val countryItem = BaseQuickPullDownFIlrateItem(this,"国家",countryAdapter)
        countryItem.setOnItemSelectListener {
            tvCountry!!.text = countryAdapter.data.get(it)
        }
        pullDown!!.addFiltrateItem(countryItem)

        val moneyAdapter = DemoAdapter(this)
        moneyAdapter.data = getData(1)
        val moneyItem = BaseQuickPullDownFIlrateItem(this,"钱",moneyAdapter)
        moneyItem.setOnItemSelectListener {
            tvMoney!!.text = moneyAdapter.data.get(it)
        }
        pullDown!!.addFiltrateItem(moneyItem)

    }

    public fun getData(type: Int): ArrayList<String>{
        if (type == 0) {
            val country = ArrayList<String>()
            country.add("china")
            country.add("france")
            country.add("russia")
            country.add("usa")
            country.add("britain")
            return country
        } else {
            val money = ArrayList<String>()
            money.add("rmb")
            money.add("euro")
            money.add("rouble")
            money.add("dollar")
            money.add("pound")
            return money
        }
    }

    class DemoAdapter: BaseQuickPullDownFIlrateItem.BaseFiltrateAdapter<DemoViewHolder>{

        var act:Activity? = null
        var data = ArrayList<String>()

        constructor(activity: Activity) {
            this.act = activity
        }

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DemoViewHolder {
            val view = LayoutInflater.from(act).inflate(R.layout.adapter_demo, null)
            return DemoViewHolder(view)
        }

        override fun onBindViewHolder(k: DemoViewHolder, i: Int) {
            super.onBindViewHolder(k, i)
            k.text!!.text = data.get(i)
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun getTitle(position: Int): String {
            return data.get(position)
        }

        override fun setSelect(helper: DemoViewHolder?, isSelect: Boolean) {
            if (isSelect) {
                helper!!.text!!.setTextColor(act!!.resources.getColor(R.color.colorAccent))
            } else{
                helper!!.text!!.setTextColor(act!!.resources.getColor(R.color.black))
            }
        }
    }

    class DemoViewHolder: RecyclerView.ViewHolder{
        var text:TextView? = null
        constructor(item: View):super(item){
            text = item.findViewById(R.id.tv_text)
        }
    }
}
