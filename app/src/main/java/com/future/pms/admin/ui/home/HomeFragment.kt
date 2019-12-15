package com.future.pms.admin.ui.home

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.future.pms.admin.R
import com.future.pms.admin.databinding.FragmentHomeBinding
import com.future.pms.admin.di.component.DaggerFragmentComponent
import com.future.pms.admin.di.module.FragmentModule
import com.future.pms.admin.model.Token
import com.future.pms.admin.model.response.ListLevel
import com.future.pms.admin.model.response.SectionDetails
import com.future.pms.admin.util.Constants.Companion.ACTIVE
import com.future.pms.admin.util.Constants.Companion.AUTHENTCATION
import com.future.pms.admin.util.Constants.Companion.EDIT_MODE
import com.future.pms.admin.util.Constants.Companion.ERROR
import com.future.pms.admin.util.Constants.Companion.EXIT_EDIT_MODE
import com.future.pms.admin.util.Constants.Companion.HOME_FRAGMENT
import com.future.pms.admin.util.Constants.Companion.SECTION_ONE
import com.future.pms.admin.util.Constants.Companion.SECTION_THREE
import com.future.pms.admin.util.Constants.Companion.SECTION_TWO
import com.future.pms.admin.util.Constants.Companion.SELECT_LEVEL
import com.future.pms.admin.util.Constants.Companion.SLOT_DISABLE
import com.future.pms.admin.util.Constants.Companion.SLOT_EMPTY
import com.future.pms.admin.util.Constants.Companion.SLOT_NULL
import com.future.pms.admin.util.Constants.Companion.SLOT_READY
import com.future.pms.admin.util.Constants.Companion.SLOT_ROAD
import com.future.pms.admin.util.Constants.Companion.SLOT_SCAN_ME
import com.future.pms.admin.util.Constants.Companion.SLOT_TAKEN
import com.future.pms.admin.util.Constants.Companion.STATUS_AVAILABLE
import com.future.pms.admin.util.Constants.Companion.STATUS_BOOKED
import com.future.pms.admin.util.Constants.Companion.STATUS_RESERVED
import com.future.pms.admin.util.Constants.Companion.STATUS_ROAD
import com.future.pms.admin.util.Constants.Companion.TOKEN
import com.future.pms.admin.util.Constants.Companion.parkMargin
import com.future.pms.admin.util.Constants.Companion.parkPadding
import com.future.pms.admin.util.Constants.Companion.parkSize
import com.future.pms.admin.util.CustomAdapter
import com.future.pms.admin.util.SpinnerItem
import com.google.gson.Gson
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class HomeFragment : Fragment(), HomeContract {
  @Inject lateinit var presenter: HomePresenter
  private lateinit var binding: FragmentHomeBinding
  private var parkViewList: MutableList<TextView> = ArrayList()
  private val spinnerItems = ArrayList<SpinnerItem>()
  private lateinit var layout: HorizontalScrollView
  private lateinit var accessToken: String
  private lateinit var idLevel: String
  private var mode = EXIT_EDIT_MODE

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
      btnEditMode.setOnClickListener {
        editMode()
      }
      btnViewSection.setOnClickListener {
        parkingLayout.visibility = View.GONE
        btnEditMode.visibility = View.GONE
        btnViewSection.visibility = View.GONE
        sectionLayout.visibility = View.VISIBLE
        btnViewLevel.visibility = View.VISIBLE
        presenter.getSectionDetails(idLevel, accessToken)
      }
      btnViewLevel.setOnClickListener {
        parkingLayout.visibility = View.VISIBLE
        btnEditMode.visibility = View.VISIBLE
        btnViewSection.visibility = View.VISIBLE
        sectionLayout.visibility = View.GONE
        btnViewLevel.visibility = View.GONE
        presenter.getParkingLayout(idLevel, accessToken)
      }
      spinnerItems.add(SpinnerItem("0", SELECT_LEVEL, true)) // Last item
      val adapter = context?.let { CustomAdapter(it, R.layout.spinner_style, spinnerItems) }
      adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      levelName.adapter = adapter
      levelName.setSelection(0)
      levelName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {
          Toast.makeText(context, "Nothing selected", Toast.LENGTH_LONG).show()
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
          val level = p0?.getItemAtPosition(p2) as SpinnerItem
          idLevel = level.idItem
          if (p2 != 0) {
            tvSelectLevel.visibility = View.GONE
            layoutPark.removeAllViews()
            layoutPark.refreshDrawableState()
            layoutPark.invalidate()
            sectionLayout.refreshDrawableState()
            sectionLayout.invalidate()

            presenter.getParkingLayout(idLevel, accessToken)
            presenter.getSectionDetails(idLevel, accessToken)
          }
        }
      }
      layout = layoutPark
      return root
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    accessToken = Gson().fromJson(
      context?.getSharedPreferences(AUTHENTCATION, Context.MODE_PRIVATE)?.getString(
        TOKEN, null
      ), Token::class.java
    ).accessToken
    presenter.attach(this)
    presenter.getLevels(accessToken)
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

  private fun showParkingLayout(slotsLayout: String) {
    binding.layoutPark.removeAllViews()
    binding.layoutPark.refreshDrawableState()
    binding.layoutPark.invalidate()
    val layoutPark = LinearLayout(context)
    var parkingLayout: LinearLayout? = null
    var totalSlot = 0
    var totalEmptySlot = 0
    var totalTakenSlot = 0
    var totalDisableSlot = 0
    val params = LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    )
    layoutPark.apply {
      orientation = LinearLayout.VERTICAL
      layoutParams = params
      setPadding(parkPadding, parkPadding, parkPadding, parkPadding)
    }
    layout.addView(layoutPark)

    for (index in 0 until slotsLayout.length) {
      totalSlot++
      if (index == 0 || totalSlot == 16) {
        totalSlot = 0
        parkingLayout = LinearLayout(context)
        parkingLayout.orientation = LinearLayout.HORIZONTAL
        layoutPark.addView(parkingLayout)
      }

      when {
        slotsLayout[index] == SLOT_NULL -> {
          setupParkingView(
            index, parkingLayout, slotsLayout[index], STATUS_ROAD, R.drawable.ic_blank
          )
        }
        slotsLayout[index] == SLOT_SCAN_ME || slotsLayout[index] == SLOT_TAKEN -> {
          totalTakenSlot += 1
          setupParkingView(
            index, parkingLayout, slotsLayout[index], STATUS_BOOKED, R.drawable.ic_car
          )
        }
        slotsLayout[index] == SLOT_EMPTY -> {
          totalEmptySlot += 1
          setupParkingView(
            index, parkingLayout, slotsLayout[index], STATUS_AVAILABLE, R.drawable.ic_park
          )
        }
        slotsLayout[index] == SLOT_DISABLE -> {
          totalDisableSlot += 1
          setupParkingView(
            index, parkingLayout, slotsLayout[index], STATUS_RESERVED, R.drawable.ic_disable
          )
        }
        slotsLayout[index] == SLOT_ROAD || slotsLayout[index] == SLOT_READY -> {
          setupParkingView(
            index, parkingLayout, slotsLayout[index], STATUS_ROAD, R.drawable.ic_road
          )
        }
      }
    }
    showTotalSlotDetail(totalDisableSlot, totalEmptySlot, totalTakenSlot)
  }

  private fun setupParkingView(
    count: Int, layout: LinearLayout?, code: Char, tags: Int, icon: Int
  ): TextView {
    val view = TextView(context)
    view.apply {
      layoutParams = LinearLayout.LayoutParams(parkSize, parkSize).apply {
        setMargins(parkMargin, parkMargin, parkMargin, parkMargin)
      }
      setPadding(0, 0, 0, 0)
      gravity = Gravity.CENTER
      setBackgroundResource(icon)
      setTextColor(resources.getColor(R.color.gold))
      tag = tags
      if (code != SLOT_NULL) {
        id = count
        text = count.toString()
      } else {
        text = ""
      }
      setOnClickListener { onClick(view) }
      setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
    }
    layout?.let {
      it.addView(view)
    }
    parkViewList.add(view)
    return view
  }

  private fun onClick(view: View) {
    if (mode == EDIT_MODE) {
      with(binding) {
        tvSelectSlot.visibility = View.GONE
        layoutSlotDetailLevel.exitEditMode.visibility = View.GONE
        editMode.visibility = View.VISIBLE
      }
      Toast.makeText(context, view.id.toString(), Toast.LENGTH_SHORT).show()
    }
  }

  private fun editMode() {
    with(binding) {
      if (mode == EDIT_MODE) {
        mode = EXIT_EDIT_MODE
        levelName.isEnabled = true
        btnEditMode.text = getString(R.string.edit_mode)
        btnViewSection.visibility = View.VISIBLE
        btnEditMode.setTextColor(resources.getColor(R.color.colorPrimary))
        editMode.visibility = View.GONE
        layoutSlotDetailLevel.exitEditMode.visibility = View.VISIBLE
        btnEditMode.visibility = View.VISIBLE
        tvSelectSlot.visibility = View.GONE
      } else {
        mode = EDIT_MODE
        levelName.isEnabled = false
        btnEditMode.text = getString(R.string.exit_edit_mode)
        btnViewSection.visibility = View.GONE
        btnEditMode.setTextColor(resources.getColor(R.color.red))
        tvSelectSlot.visibility = View.VISIBLE
        layoutSlotDetailLevel.exitEditMode.visibility = View.INVISIBLE
      }
    }
  }

  private fun showTotalSlotDetail(totalDisableSlot: Int, totalEmptySlot: Int, totalTakenSlot: Int) {
    with(binding.layoutSlotDetailLevel) {
      disableSlotValue.text = totalDisableSlot.toString()
      emptySlotValue.text = totalEmptySlot.toString()
      takenSlotValue.text = totalTakenSlot.toString()
    }
  }

  override fun getLayoutSuccess(slotsLayout: String) {
    showParkingLayout(slotsLayout)
  }

  override fun getLevelsSuccess(listLevel: List<ListLevel>) {
    for (index in 0 until listLevel.size) {
      spinnerItems.add(
        index + 1, SpinnerItem(listLevel[index].idLevel, listLevel[index].levelName, false)
      )
    }
  }

  override fun getSectionDetailsSuccess(listSectionDetails: List<SectionDetails>) {
    for (index in 0 until listSectionDetails.size) {
      when {
        listSectionDetails[index].sectionName == SECTION_ONE -> {
          with(binding) {
            with(detailSection1) {
              disableSlotValue.text = listSectionDetails[index].totalDisableSlot.toString()
              emptySlotValue.text = listSectionDetails[index].totalEmptySlot.toString()
              takenSlotValue.text = listSectionDetails[index].totalTakenSlot.toString()
            }
            with(btnSection1) {
              text = if (listSectionDetails[index].status == ACTIVE) {
                setBackgroundResource(R.drawable.card_layout_red_right_radius)
                getString(R.string.deactivate)
              } else {
                setBackgroundResource(R.drawable.card_layout_blue_primary_right_radius)
                getString(R.string.activate)
              }
              setOnClickListener {
                presenter.updateParkingSection(
                  listSectionDetails[index].idSection, accessToken
                )
              }
            }
          }
        }
        listSectionDetails[index].sectionName == SECTION_TWO -> {
          with(binding) {
            with(detailSection2) {
              disableSlotValue.text = listSectionDetails[index].totalDisableSlot.toString()
              emptySlotValue.text = listSectionDetails[index].totalEmptySlot.toString()
              takenSlotValue.text = listSectionDetails[index].totalTakenSlot.toString()
            }
            with(btnSection2) {
              text = if (listSectionDetails[index].status == ACTIVE) {
                setBackgroundResource(R.drawable.card_layout_red_right_radius)
                getString(R.string.deactivate)
              } else {
                setBackgroundResource(R.drawable.card_layout_blue_primary_right_radius)
                getString(R.string.activate)
              }
              setOnClickListener {
                presenter.updateParkingSection(
                  listSectionDetails[index].idSection, accessToken
                )
              }
            }
          }
        }
        listSectionDetails[index].sectionName == SECTION_THREE -> {
          with(binding) {
            with(detailSection3) {
              disableSlotValue.text = listSectionDetails[index].totalDisableSlot.toString()
              emptySlotValue.text = listSectionDetails[index].totalEmptySlot.toString()
              takenSlotValue.text = listSectionDetails[index].totalTakenSlot.toString()
            }
            with(btnSection3) {
              text = if (listSectionDetails[index].status == ACTIVE) {
                setBackgroundResource(R.drawable.card_layout_red_right_radius)
                getString(R.string.deactivate)
              } else {
                setBackgroundResource(R.drawable.card_layout_blue_primary_right_radius)
                getString(R.string.activate)
              }
              setOnClickListener {
                presenter.updateParkingSection(
                  listSectionDetails[index].idSection, accessToken
                )
              }
            }
          }
        }
        else -> {
          with(binding) {
            with(detailSection4) {
              disableSlotValue.text = listSectionDetails[index].totalDisableSlot.toString()
              emptySlotValue.text = listSectionDetails[index].totalEmptySlot.toString()
              takenSlotValue.text = listSectionDetails[index].totalTakenSlot.toString()
            }
            with(btnSection4) {
              text = if (listSectionDetails[index].status == ACTIVE) {
                setBackgroundResource(R.drawable.card_layout_red_right_radius)
                getString(R.string.deactivate)
              } else {
                setBackgroundResource(R.drawable.card_layout_blue_primary_right_radius)
                getString(R.string.activate)
              }
              setOnClickListener {
                presenter.updateParkingSection(
                  listSectionDetails[index].idSection, accessToken
                )
              }
            }
          }
        }
      }
    }
  }

  override fun updateParkingSectionSuccess(response: String) {
    presenter.getSectionDetails(idLevel, accessToken)
    presenter.getParkingLayout(idLevel, accessToken)
  }

  override fun getLayoutFailed(error: String) {
    Timber.tag(ERROR).e(error)
    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
  }

  override fun showErrorMessage(error: String) {
    Timber.tag(ERROR).e(error)
    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
  }

  private fun injectDependency() {
    val profileComponent = DaggerFragmentComponent.builder().fragmentModule(
      FragmentModule()
    ).build()
    profileComponent.inject(this)
  }
}