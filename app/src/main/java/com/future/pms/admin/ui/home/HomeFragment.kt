package com.future.pms.admin.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.method.ScrollingMovementMethod
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.future.pms.admin.R
import com.future.pms.admin.databinding.BottomsheetHomeBinding
import com.future.pms.admin.databinding.FragmentHomeBinding
import com.future.pms.admin.databinding.SlotDetailShortBinding
import com.future.pms.admin.di.component.DaggerFragmentComponent
import com.future.pms.admin.di.module.FragmentModule
import com.future.pms.admin.model.Token
import com.future.pms.admin.model.response.ListLevel
import com.future.pms.admin.model.response.SectionDetails
import com.future.pms.admin.ui.main.MainActivity
import com.future.pms.admin.util.Constants.Companion.ACTIVE
import com.future.pms.admin.util.Constants.Companion.AUTHENTCATION
import com.future.pms.admin.util.Constants.Companion.EDIT_MODE
import com.future.pms.admin.util.Constants.Companion.ERROR
import com.future.pms.admin.util.Constants.Companion.EXIT_EDIT_MODE
import com.future.pms.admin.util.Constants.Companion.HOME_FRAGMENT
import com.future.pms.admin.util.Constants.Companion.LEVEL_UNAVAILABLE
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
import com.future.pms.admin.util.Utils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class HomeFragment : Fragment(), HomeContract {
  @Inject lateinit var presenter: HomePresenter
  private var parkViewList: MutableList<TextView> = ArrayList()
  private val spinnerItems = ArrayList<SpinnerItem>()
  private val handler = Handler()
  private var isSyncOn = false
  private var mode = EXIT_EDIT_MODE
  private lateinit var binding: FragmentHomeBinding
  private lateinit var bindingHome: BottomsheetHomeBinding
  private lateinit var layout: HorizontalScrollView
  private lateinit var accessToken: String
  private lateinit var idLevel: String
  private lateinit var nameLevel: String
  private lateinit var levelStatus: String
  private lateinit var levelLayout: String
  private lateinit var mBottomSheetBehavior: BottomSheetBehavior<View>

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

  override fun onPause() {
    if (mode == EDIT_MODE) {
      editMode()
    }
    super.onPause()
  }

  @SuppressLint("ClickableViewAccessibility") override fun onCreateView(inflater: LayoutInflater,
      container: ViewGroup?, savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, null, false)
    bindingHome = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_home, container, false)
    val bottomSheet: View = bindingHome.addLevel.bottomSheet
    mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

    with(bindingHome.home) {
      val adapter = context?.let { CustomAdapter(it, R.layout.spinner_style, spinnerItems) }
      adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      spinnerItems.add(SpinnerItem("0", SELECT_LEVEL, "", true)) // First item
      levelName.adapter = adapter
      levelName.setSelection(0)
      levelName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {
          Toast.makeText(context, getString(R.string.nothing_selected), Toast.LENGTH_LONG).show()
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
          val level = p0?.getItemAtPosition(p2) as SpinnerItem
          idLevel = level.idItem
          nameLevel = level.itemString
          levelStatus = level.itemStatus

          if (p2 != 0) {
            tvSelectLevel.visibility = View.GONE
            ivSelectLevel.visibility = View.GONE
            btnViewSection.isEnabled = true
            btnViewSection.setTextColor(resources.getColor(R.color.colorAccent))
            btnEditMode.isEnabled = true
            btnEditMode.setTextColor(resources.getColor(R.color.colorPrimary))
            btnSync.visibility = View.VISIBLE
            btnEditLevel.visibility = View.VISIBLE
            layoutPark.removeAllViews()
            layoutPark.refreshDrawableState()
            layoutPark.invalidate()
            sectionLayout.refreshDrawableState()
            sectionLayout.invalidate()

            presenter.getParkingLayout(idLevel, accessToken)
            presenter.getSectionDetails(idLevel, accessToken)

            if (levelStatus == LEVEL_UNAVAILABLE) {
              tvUnavailableTag.visibility = View.VISIBLE
            } else {
              tvUnavailableTag.visibility = View.GONE
            }
          }
        }
      }
      layout = layoutPark

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

      btnEditLevel.setOnClickListener {
        val activity = activity as MainActivity?
        activity?.presenter?.showEditLevel(idLevel, nameLevel, levelStatus)
      }

      btnSync.setOnClickListener {
        if (isSyncOn) {
          isSyncOn = false
          btnSync.setImageResource(R.drawable.ic_sync_off)
          handler.removeCallbacksAndMessages(null)
        } else {
          isSyncOn = true
          btnSync.setImageResource(R.drawable.ic_sync)
          handler.postDelayed(object : Runnable {
            override fun run() {
              presenter.getParkingLayout(idLevel, accessToken)
              handler.postDelayed(this, 5000)
            }
          }, 5000)
          Toast.makeText(context, getString(R.string.automatically_refresh_5s),
              Toast.LENGTH_SHORT).show()
        }
      }

      btnAddLevel.setOnClickListener {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
      }
    }

    with(bindingHome) {
      addLevel.aboutLevel.movementMethod = ScrollingMovementMethod()
      addLevel.aboutLevel.setOnTouchListener { v, event ->
        v.parent.requestDisallowInterceptTouchEvent(true)
        when (event.action and MotionEvent.ACTION_MASK) {
          MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
        }
        false
      }

      home.btnSave.setOnClickListener {
        presenter.updateLevel(idLevel, levelLayout, accessToken)
      }

      addLevel.btnCreate.setOnClickListener {
        if (isValid()) {
          presenter.addParkingLevel(bindingHome.addLevel.txtLevelName.text.toString(), accessToken)
        } else {
          Toast.makeText(context, getString(R.string.fill_all_the_entries),
              Toast.LENGTH_LONG).show()
        }
      }
      return root
    }
  }

  private fun isValid(): Boolean {
    if (bindingHome.addLevel.txtLevelName.text.toString().isEmpty()) return false
    if (!bindingHome.addLevel.cbIHaveRead.isChecked) return false
    return true
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    accessToken = Gson().fromJson(
        context?.getSharedPreferences(AUTHENTCATION, Context.MODE_PRIVATE)?.getString(TOKEN, null),
        Token::class.java).accessToken
    presenter.attach(this)
    presenter.getLevels(accessToken)
  }

  private fun showParkingLayout(slotsLayout: String) {
    bindingHome.home.layoutPark.removeAllViews()
    bindingHome.home.layoutPark.refreshDrawableState()
    bindingHome.home.layoutPark.invalidate()

    val layoutPark = LinearLayout(context)
    var parkingLayout: LinearLayout? = null
    var totalSlot = 0
    var totalEmptySlot = 0
    var totalTakenSlot = 0
    var totalDisableSlot = 0
    val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT)

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
          setupParkingView(index, parkingLayout, slotsLayout[index], STATUS_ROAD,
              R.drawable.ic_blank)
        }
        slotsLayout[index] == SLOT_SCAN_ME || slotsLayout[index] == SLOT_TAKEN -> {
          totalTakenSlot += 1
          setupParkingView(index, parkingLayout, slotsLayout[index], STATUS_BOOKED,
              R.drawable.ic_car)
        }
        slotsLayout[index] == SLOT_EMPTY -> {
          totalEmptySlot += 1
          setupParkingView(index, parkingLayout, slotsLayout[index], STATUS_AVAILABLE,
              R.drawable.ic_park)
        }
        slotsLayout[index] == SLOT_DISABLE -> {
          totalDisableSlot += 1
          setupParkingView(index, parkingLayout, slotsLayout[index], STATUS_RESERVED,
              R.drawable.ic_disable)
        }
        slotsLayout[index] == SLOT_ROAD || slotsLayout[index] == SLOT_READY -> {
          setupParkingView(index, parkingLayout, slotsLayout[index], STATUS_ROAD,
              R.drawable.ic_road)
        }
      }
    }
    showTotalSlotDetail(totalDisableSlot, totalEmptySlot, totalTakenSlot)
  }

  private fun setupParkingView(count: Int, layout: LinearLayout?, code: Char, tags: Int,
      icon: Int): TextView {
    val view = TextView(context)
    view.apply {
      layoutParams = LinearLayout.LayoutParams(parkSize, parkSize).apply {
        setMargins(parkMargin, parkMargin, parkMargin, parkMargin)
      }

      setPadding(0, 0, 0, 0)
      gravity = Gravity.CENTER
      setBackgroundResource(icon)
      if (code != SLOT_NULL) {
        id = count
      }

      if (icon == R.drawable.ic_road) {
        setTextColor(resources.getColor(R.color.colorPrimaryDark))
        text = ((id % 16) + 1).toString()
      }
      setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
      setOnClickListener { onClick(view) }
    }

    layout?.let {
      it.addView(view)
    }

    parkViewList.add(view)
    return view
  }

  private fun onClick(view: View) {
    if (mode == EDIT_MODE) {
      if (view.id == -1) {
        Toast.makeText(context, getString(R.string.activate_this_section),
            Toast.LENGTH_SHORT).show()
      } else {
        with(bindingHome.home, {
          tvSelectSlot.visibility = View.GONE
          layoutSlotDetailLevel.exitEditMode.visibility = View.GONE
          editMode.visibility = View.VISIBLE
          btnSave.visibility = View.VISIBLE
          slotName.text = view.id.toString()

          if (levelLayout[view.id] == SLOT_TAKEN) {
            context?.let {
              Utils.simpleDialogMessage(it, String.format(getString(R.string.two_value_comma),
                  getString(R.string.slot_number), view.id),
                  getString(R.string.force_update_message))
            }
          }
          statusDisable.setOnClickListener {
            changeSlot(levelLayout, SLOT_DISABLE, view.id)
            view.setBackgroundResource(R.drawable.ic_disable)
          }
          statusPark.setOnClickListener {
            changeSlot(levelLayout, SLOT_EMPTY, view.id)
            view.setBackgroundResource(R.drawable.ic_park)
          }
          statusRoad.setOnClickListener {
            changeSlot(levelLayout, SLOT_ROAD, view.id)
            view.setBackgroundResource(R.drawable.ic_road)
          }
        })
      }
    }
  }

  private fun changeSlot(levelLayout: String, status: Char, index: Int) {
    val levelLayoutStrBuilder = StringBuilder(levelLayout)
    levelLayoutStrBuilder.setCharAt(index, status)
    this@HomeFragment.levelLayout = levelLayoutStrBuilder.toString()
  }

  private fun editMode() {
    with(bindingHome.home) {
      if (mode == EDIT_MODE) {
        btnSync.visibility = View.VISIBLE
        btnEditLevel.visibility = View.VISIBLE
        mode = EXIT_EDIT_MODE
        levelName.isEnabled = true
        btnEditMode.text = getString(R.string.edit_mode)
        btnSave.visibility = View.GONE
        btnViewSection.visibility = View.VISIBLE
        btnEditMode.setTextColor(resources.getColor(R.color.colorPrimary))
        editMode.visibility = View.GONE
        layoutSlotDetailLevel.exitEditMode.visibility = View.VISIBLE
        btnEditMode.visibility = View.VISIBLE
        tvSelectSlot.visibility = View.GONE
        presenter.editModeParkingLevel(idLevel, mode, accessToken)
      } else {
        if (isSyncOn) {
          isSyncOn = false
          btnSync.setImageResource(R.drawable.ic_sync_off)
          handler.removeCallbacksAndMessages(null)
        }
        btnSync.visibility = View.INVISIBLE
        btnEditLevel.visibility = View.INVISIBLE
        mode = EDIT_MODE
        levelName.isEnabled = false
        btnEditMode.text = getString(R.string.exit_edit_mode)
        btnViewSection.visibility = View.GONE
        btnEditMode.setTextColor(resources.getColor(R.color.red))
        tvSelectSlot.visibility = View.VISIBLE
        layoutSlotDetailLevel.exitEditMode.visibility = View.INVISIBLE
        presenter.editModeParkingLevel(idLevel, mode, accessToken)
      }
    }
    presenter.getParkingLayout(idLevel, accessToken)
  }

  private fun showTotalSlotDetail(totalDisableSlot: Int, totalEmptySlot: Int, totalTakenSlot: Int) {
    with(bindingHome.home.layoutSlotDetailLevel) {
      disableSlotValue.text = totalDisableSlot.toString()
      emptySlotValue.text = totalEmptySlot.toString()
      takenSlotValue.text = totalTakenSlot.toString()
    }
  }

  override fun editModeParkingLevelSuccess(response: String) {
    Timber.tag(TAG).d(response)
  }

  override fun addParkingLevelSuccess(response: String) {
    Toast.makeText(context, getString(R.string.success_create_level), Toast.LENGTH_LONG).show()
    refreshPage()
    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    bindingHome.addLevel.txtLevelName.text?.clear()
    bindingHome.addLevel.cbIHaveRead.isChecked = false
  }

  override fun getLayoutSuccess(slotsLayout: String) {
    levelLayout = slotsLayout
    showParkingLayout(slotsLayout)
  }

  override fun getLevelsSuccess(listLevel: List<ListLevel>) {
    spinnerItems.clear()
    spinnerItems.add(SpinnerItem("0", SELECT_LEVEL, "", true)) // First item
    for (index in 0 until listLevel.size) {
      spinnerItems.add(index + 1, SpinnerItem(listLevel[index].idLevel, listLevel[index].levelName,
          listLevel[index].levelStatus, false))
    }
  }

  override fun getSectionDetailsSuccess(listSectionDetails: List<SectionDetails>) {
    for (index in 0 until listSectionDetails.size) {
      when {
        listSectionDetails[index].sectionName == SECTION_ONE -> {
          with(bindingHome.home) {
            with(detailSection1) {
              setupSectionDetails(listSectionDetails, index)
            }
            with(btnSection1) {
              setupSectionButton(listSectionDetails, index)
            }
          }
        }
        listSectionDetails[index].sectionName == SECTION_TWO -> {
          with(bindingHome.home) {
            with(detailSection2) {
              setupSectionDetails(listSectionDetails, index)
            }
            with(btnSection2) {
              setupSectionButton(listSectionDetails, index)
            }
          }
        }
        listSectionDetails[index].sectionName == SECTION_THREE -> {
          with(bindingHome.home) {
            with(detailSection3) {
              setupSectionDetails(listSectionDetails, index)
            }
            with(btnSection3) {
              setupSectionButton(listSectionDetails, index)
            }
          }
        }
        else -> {
          with(bindingHome.home) {
            with(detailSection4) {
              setupSectionDetails(listSectionDetails, index)
            }
            with(btnSection4) {
              setupSectionButton(listSectionDetails, index)
            }
          }
        }
      }
    }
  }

  private fun SlotDetailShortBinding.setupSectionDetails(listSectionDetails: List<SectionDetails>,
      index: Int) {
    disableSlotValue.text = listSectionDetails[index].totalDisableSlot.toString()
    emptySlotValue.text = listSectionDetails[index].totalEmptySlot.toString()
    takenSlotValue.text = listSectionDetails[index].totalTakenSlot.toString()
  }

  private fun Button.setupSectionButton(listSectionDetails: List<SectionDetails>, index: Int) {
    text = if (listSectionDetails[index].status == ACTIVE) {
      setBackgroundResource(R.drawable.card_layout_red_right_radius)
      getString(R.string.deactivate)
    } else {
      setBackgroundResource(R.drawable.card_layout_blue_primary_right_radius)
      getString(R.string.activate)
    }
    setOnClickListener {
      if (listSectionDetails[index].status == ACTIVE) {
        if (listSectionDetails[index].totalTakenSlot > 0) {
          showCantDeactivateDialog()
        } else {
          showConfirmationDialog(listSectionDetails, index)
        }
      } else {
        presenter.updateParkingSection(listSectionDetails[index].idSection, accessToken)
      }
    }
  }

  private fun Button.showConfirmationDialog(listSectionDetails: List<SectionDetails>, index: Int) {
    context?.let {
      AlertDialog.Builder(it).setTitle(getString(R.string.deactivate_section)).setMessage(
          getString(R.string.deactivate_section_confirmation)).setPositiveButton(
          android.R.string.yes) { _, _ ->
        presenter.updateParkingSection(listSectionDetails[index].idSection, accessToken)
      }.setNegativeButton(android.R.string.no, null).setIcon(R.drawable.ic_arrow).show()
    }
  }

  private fun Button.showCantDeactivateDialog() {
    context?.let {
      AlertDialog.Builder(it).setTitle(getString(R.string.cant_deactivate_section)).setMessage(
          getString(R.string.still_have_ongoin_this_section)).setPositiveButton(
          android.R.string.yes, null).setIcon(R.drawable.ic_arrow).show()
    }
  }

  override fun updateParkingLayoutSuccess(response: String) {
    editMode()
    Toast.makeText(context, response, Toast.LENGTH_LONG).show()
  }

  override fun updateParkingSectionSuccess(response: String) {
    presenter.getSectionDetails(idLevel, accessToken)
    presenter.getParkingLayout(idLevel, accessToken)
  }

  fun refreshPage() {
    val ft = fragmentManager?.beginTransaction()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      ft?.setReorderingAllowed(false)
    }
    ft?.detach(this)?.attach(this)?.commit()
    bindingHome.home.levelName.setSelection(0)
  }

  override fun getLayoutFailed(error: String) {
    Timber.tag(ERROR).e(error)
  }

  override fun showErrorMessage(error: String) {
    Timber.tag(ERROR).e(error)
    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
  }

  private fun injectDependency() {
    val profileComponent = DaggerFragmentComponent.builder().fragmentModule(
        FragmentModule()).build()
    profileComponent.inject(this)
  }
}