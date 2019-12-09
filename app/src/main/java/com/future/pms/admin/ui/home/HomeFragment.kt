package com.future.pms.admin.ui.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.future.pms.admin.R
import com.future.pms.admin.databinding.FragmentHomeBinding
import com.future.pms.admin.di.component.DaggerFragmentComponent
import com.future.pms.admin.di.module.FragmentModule
import com.future.pms.admin.model.oauth.Token
import com.future.pms.admin.util.Constants
import com.future.pms.admin.util.Constants.Companion.HOME_FRAGMENT
import com.google.gson.Gson
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class HomeFragment : Fragment(), HomeContract {
  @Inject lateinit var presenter: HomePresenter
  private lateinit var binding: FragmentHomeBinding
  private var parkViewList: MutableList<TextView> = ArrayList()
  private var SLOTS =
    ("/\$_UUAAU_RR_UU_UU_/" + "___Z_____________/" + "_AARAU_UU_UU_UU_/" + "_UUARR_RR_UU_AR_/" + "________________/" + "_URAAU_RA_UU_UU_/" + "_RUUAU_RR_UU_UU_/" + "________________/" + "_UU_AU_RU_UR_UU_/" + "_UU_AU_RR_AR_UU_/" + "________________/" + "_UURAUARRAUUAUU_/" + "________________/" + "_URRAUARARUURUU_/" + "________________/")

  companion object {
    const val TAG: String = HOME_FRAGMENT
  }

  fun newInstance(): HomeFragment {
    return HomeFragment()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injectDependency()
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
    with(binding) {
      val layout = layoutPark
      showParkingLayout(layout)
      return root
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val accessToken = Gson().fromJson(
      context?.getSharedPreferences(Constants.AUTHENTCATION, Context.MODE_PRIVATE)?.getString(
        Constants.TOKEN, null
      ), Token::class.java
    ).accessToken
    presenter.attach(this)
  }

  override fun showProgress(show: Boolean) {
    /*if (null != progressBar) {
      if (show) {
        progressBar.visibility = View.VISIBLE
      } else {
        progressBar.visibility = View.GONE
      }
    }*/
  }

  private fun showParkingLayout(layout: HorizontalScrollView) {
    val layoutPark = LinearLayout(context)
    var parkingLayout: LinearLayout? = null
    var count = 0
    val params = LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    )
    layoutPark.apply {
      orientation = LinearLayout.VERTICAL
      layoutParams = params
      setPadding(
        Constants.parkPadding, Constants.parkPadding, Constants.parkPadding, Constants.parkPadding
      )
    }
    layout.addView(layoutPark)

    for (index in 0 until SLOTS.length) {
      when {
        SLOTS[index] == '/' -> {
          parkingLayout = LinearLayout(context)
          parkingLayout.orientation = LinearLayout.HORIZONTAL
          layoutPark.addView(parkingLayout)
        }
        SLOTS[index] == 'U' -> {
          count++
          setupParkingView(
            count, parkingLayout, SLOTS[index], Constants.STATUS_BOOKED, R.drawable.ic_car
          )
        }
        SLOTS[index] == 'A' -> {
          count++
          setupParkingView(
            count, parkingLayout, SLOTS[index], Constants.STATUS_AVAILABLE, R.drawable.ic_park
          )
        }
        SLOTS[index] == 'R' -> {
          count++
          setupParkingView(
            count, parkingLayout, SLOTS[index], Constants.STATUS_RESERVED, R.drawable.ic_disable
          )
        }
        SLOTS[index] == '_' -> {
          setupParkingView(
            count, parkingLayout, SLOTS[index], Constants.STATUS_ROAD, R.drawable.ic_road
          )
        }
      }
    }
  }

  private fun setupParkingView(
    count: Int, layout: LinearLayout?, code: Char, tags: Int, icon: Int
  ): TextView {
    val view = TextView(context)
    view.apply {
      layoutParams = LinearLayout.LayoutParams(Constants.parkSize, Constants.parkSize).apply {
        setMargins(
          Constants.parkMargin, Constants.parkMargin, Constants.parkMargin, Constants.parkMargin
        )
      }
      setPadding(0, 0, 0, 0)
      gravity = Gravity.CENTER
      setBackgroundResource(icon)
      setTextColor(Color.WHITE)
      tag = tags
      if (code != '_') {
        id = count
        text = count.toString()
        setOnClickListener { onClick(view) }
      } else {
        text = ""
      }
      setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
    }
    layout?.let {
      it.addView(view)
    }
    parkViewList.add(view)
    return view
  }

  private fun onClick(view: View) {
    if (view.tag as Int == Constants.STATUS_AVAILABLE) {
      if (Constants.selectedIds.contains(view.id.toString() + ",")) {
        Constants.selectedIds = Constants.selectedIds.replace((+view.id).toString() + ",", "")
        view.setBackgroundResource(R.drawable.ic_park)
      } else {
        Constants.selectedIds = Constants.selectedIds + view.id + ","
        view.setBackgroundResource(R.drawable.ic_my_location)
      }
    } else if (view.tag as Int == Constants.STATUS_BOOKED) {
      Toast.makeText(
        context, String.format(getString(R.string.park_is_booked), view.id), Toast.LENGTH_SHORT
      ).show()
    } else if (view.tag as Int == Constants.STATUS_RESERVED) {
      Toast.makeText(
        context, String.format(getString(R.string.park_is_reserved), view.id), Toast.LENGTH_SHORT
      ).show()
    }
  }

  override fun showErrorMessage(error: String) {
    Timber.tag(Constants.ERROR).e(error)
  }

  private fun injectDependency() {
    val profileComponent = DaggerFragmentComponent.builder().fragmentModule(
      FragmentModule()
    ).build()
    profileComponent.inject(this)
  }
}