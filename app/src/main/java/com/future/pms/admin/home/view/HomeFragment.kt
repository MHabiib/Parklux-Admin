package com.future.pms.admin.home.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.method.ScrollingMovementMethod
import android.util.TypedValue
import android.view.*
import android.view.View.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.future.pms.admin.BaseApp
import com.future.pms.admin.R
import com.future.pms.admin.core.base.BaseFragment
import com.future.pms.admin.core.model.Token
import com.future.pms.admin.core.model.response.ListLevel
import com.future.pms.admin.core.model.response.SectionDetails
import com.future.pms.admin.databinding.BottomsheetHomeBinding
import com.future.pms.admin.databinding.FragmentHomeBinding
import com.future.pms.admin.databinding.SlotDetailShortBinding
import com.future.pms.admin.home.injection.DaggerHomeComponent
import com.future.pms.admin.home.injection.HomeComponent
import com.future.pms.admin.home.presenter.HomePresenter
import com.future.pms.admin.main.view.MainActivity
import com.future.pms.admin.util.Constants
import com.future.pms.admin.util.Constants.Companion.ACTIVE
import com.future.pms.admin.util.Constants.Companion.AUTHENTICATION
import com.future.pms.admin.util.Constants.Companion.EDIT_MODE
import com.future.pms.admin.util.Constants.Companion.ERROR
import com.future.pms.admin.util.Constants.Companion.EXIT_EDIT_MODE
import com.future.pms.admin.util.Constants.Companion.HOME_FRAGMENT
import com.future.pms.admin.util.Constants.Companion.LEVEL_UNAVAILABLE
import com.future.pms.admin.util.Constants.Companion.SECTION_ONE
import com.future.pms.admin.util.Constants.Companion.SECTION_THREE
import com.future.pms.admin.util.Constants.Companion.SECTION_TWO
import com.future.pms.admin.util.Constants.Companion.SELECT_LEVEL
import com.future.pms.admin.util.Constants.Companion.SLOT_BLOCK
import com.future.pms.admin.util.Constants.Companion.SLOT_DISABLE
import com.future.pms.admin.util.Constants.Companion.SLOT_EMPTY
import com.future.pms.admin.util.Constants.Companion.SLOT_IN
import com.future.pms.admin.util.Constants.Companion.SLOT_NULL
import com.future.pms.admin.util.Constants.Companion.SLOT_OUT
import com.future.pms.admin.util.Constants.Companion.SLOT_READY
import com.future.pms.admin.util.Constants.Companion.SLOT_ROAD
import com.future.pms.admin.util.Constants.Companion.SLOT_SCAN_ME
import com.future.pms.admin.util.Constants.Companion.SLOT_TAKEN
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
import java.lang.ref.WeakReference
import java.util.*
import javax.inject.Inject

class HomeFragment : BaseFragment(), HomeContract {
  private var daggerBuild: HomeComponent = DaggerHomeComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: HomePresenter
  @Inject lateinit var gson: Gson
  private val spinnerItems = ArrayList<SpinnerItem>()
  private lateinit var adapter: CustomAdapter
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
  private lateinit var layoutPark: LinearLayout
  private lateinit var parkingLayout: LinearLayout
  private var totalTakenSlot = 0
  private var totalEmptySlot = 0
  private var totalDisableSlot = 0
  private var totalRow = 0
  private lateinit var asyncTask: SetupLayoutAsyc

  companion object {
    private val LETTER = ArrayList(
        listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
            "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "A1", "A2", "A3", "A4", "A5", "A6", "A7",
            "A8", "A9", "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "C1", "C2", "C3",
            "C4", "C5", "C6", "C7", "C8", "C9", "D1", "D2", "D3", "D4", "D5", "D6", "D7"))
    private const val TOTAL_SLOTS_IN_ROW = 60
    const val TAG: String = HOME_FRAGMENT
  }

  fun newInstance(): HomeFragment = HomeFragment()

  override fun onPause() {
    if (mode == EDIT_MODE) {
      presenter.editModeParkingLevel(idLevel, EXIT_EDIT_MODE, accessToken)
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
      context?.let {
        adapter = CustomAdapter(it, R.layout.spinner_style, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      }
      spinnerItems.add(SpinnerItem("0", SELECT_LEVEL, "")) // First item
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
            tvAppName.visibility = GONE
            tvAddLevel.visibility = GONE
            ivAddLevel.visibility = GONE
            ivSelectLevelLeft.visibility = GONE
            svParkingSlot.visibility = VISIBLE
            btnViewSection.isEnabled = true
            btnViewSection.setTextColor(resources.getColor(R.color.colorAccent))
            btnEditMode.isEnabled = true
            btnSave.isEnabled = true
            btnEditMode.setTextColor(resources.getColor(R.color.colorPrimary))
            btnSync.visibility = VISIBLE
            btnEditLevel.visibility = VISIBLE
            layoutPark.removeAllViews()
            layoutPark.refreshDrawableState()
            layoutPark.invalidate()
            sectionLayout.refreshDrawableState()
            sectionLayout.invalidate()
            showProgress(true)
            asyncTask.cancel(true)
            presenter.getParkingLayout(idLevel, accessToken)
            presenter.getSectionDetails(idLevel, accessToken)

            if (levelStatus == LEVEL_UNAVAILABLE) {
              tvUnavailableTag.visibility = VISIBLE
            } else {
              tvUnavailableTag.visibility = GONE
            }
          }
        }
      }
      layout = layoutPark

      btnEditMode.setOnClickListener {
        asyncTask.cancel(true)
        editMode()
      }

      btnViewSection.setOnClickListener {
        parkingLayout.visibility = GONE
        btnEditMode.visibility = GONE
        btnViewSection.visibility = GONE
        btnViewLevel.visibility = VISIBLE
        sectionLayout.visibility = VISIBLE
        if (::asyncTask.isInitialized) {
          asyncTask.cancel(true)
        }
        presenter.getSectionDetails(idLevel, accessToken)
      }

      btnViewLevel.setOnClickListener {
        parkingLayout.visibility = VISIBLE
        if (levelStatus != LEVEL_UNAVAILABLE) {
          btnEditMode.visibility = VISIBLE
        }
        btnViewSection.visibility = VISIBLE
        sectionLayout.visibility = GONE
        if (::asyncTask.isInitialized) {
          asyncTask.cancel(true)
        }
        btnViewLevel.visibility = GONE
        showProgress(true)
        presenter.getParkingLayout(idLevel, accessToken)
      }

      btnEditLevel.setOnClickListener {
        val activity = activity as MainActivity?
        activity?.presenter?.showEditLevel(idLevel, nameLevel, levelStatus, totalTakenSlot)
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
              showProgress(true)
              presenter.getParkingLayout(idLevel, accessToken)
              handler.postDelayed(this, 20000)
            }
          }, 20000)
          Toast.makeText(context, getString(R.string.automatically_refresh_20s),
              Toast.LENGTH_SHORT).show()
        }
      }

      btnAddLevel.setOnClickListener {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
      }

      ivAddLevel.setOnClickListener {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
      }

      ibInfo.setOnClickListener {
        context?.let { context ->
          AlertDialog.Builder(context).apply {
            setView(inflate(context, R.layout.layout_info_dialog, null))
          }.show()
        }
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
        asyncTask.cancel(true)
        showProgress(true)
        home.btnSave.isEnabled = false
        home.btnEditMode.isEnabled = false
        presenter.updateLevel(idLevel, levelLayout, accessToken)
      }

      addLevel.btnCreate.setOnClickListener {
        if (isValid()) {
          addLevel.btnCreate.isEnabled = false
          presenter.addParkingLevel(bindingHome.addLevel.txtLevelName.text.toString(), accessToken)
        } else {
          Toast.makeText(context, getString(R.string.fill_all_the_entries),
              Toast.LENGTH_LONG).show()
        }
      }
    }
    return bindingHome.root
  }

  private fun isValid(): Boolean {
    if (bindingHome.addLevel.txtLevelName.text.toString().isEmpty()) return false
    if (!bindingHome.addLevel.cbIHaveRead.isChecked) return false
    return true
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    accessToken = gson.fromJson(
        context?.getSharedPreferences(AUTHENTICATION, Context.MODE_PRIVATE)?.getString(TOKEN, null),
        Token::class.java).accessToken
    presenter.attach(this)
    presenter.getLevels(accessToken)
    totalRow = 0
    asyncTask = SetupLayoutAsyc(activity as MainActivity)
  }

  private fun showParkingLayout(slotsLayout: String) {
    bindingHome.home.layoutPark.removeAllViews()
    bindingHome.home.layoutPark.refreshDrawableState()
    bindingHome.home.layoutPark.invalidate()

    totalTakenSlot = 0
    totalEmptySlot = 0
    totalDisableSlot = 0

    layoutPark = LinearLayout(context)
    val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT)

    layoutPark.apply {
      orientation = LinearLayout.VERTICAL
      layoutParams = params
      setPadding(parkPadding, parkPadding, parkPadding, parkPadding)
    }
    layout.addView(layoutPark)

    totalRow = 0
    asyncTask = SetupLayoutAsyc(activity as MainActivity)
    asyncTask.execute(slotsLayout)
  }

  private class SetupLayoutAsyc internal constructor(context: MainActivity) :
      AsyncTask<String, String, String>() {
    private val activityReference: WeakReference<MainActivity> = WeakReference(context)
    private val homeFragment = activityReference.get()?.supportFragmentManager?.run {
      findFragmentByTag(TAG)
    }
    private val mHomeFragment = homeFragment as HomeFragment

    override fun doInBackground(vararg params: String?): String? {
      val slots = params[0]
      if (slots != null) {
        var index = 0
        while (index in 0 until slots.length) {
          if (index == 1800 && slots[1800] == '_' && slots[1830] == '_') {
            index = slots.length - 1
          }
          if (index % 30 == 0 && slots[index] == '_' && index != 0 && index % 60 != 0) {
            index += 29
          }
          if (index % 60 == 0) {
            Thread.sleep(100)
          }
          publishProgress("$index${slots[index]}")
          index++
        }
      }
      return ""
    }

    override fun onProgressUpdate(vararg result: String?) {
      if (homeFragment != null && activityReference.get() != null) {
        mHomeFragment.setSlotStatus(result[0])
      }
    }
  }

  fun setSlotStatus(result: String?) {
    if (result != "" && context != null) {
      val slotsLayout = result?.substring(result.length - 1)?.single()
      val index = result?.substring(0, result.length - 1)?.toInt()

      if (index != null) {
        if (index == 0 || index % 60 == 0) {
          parkingLayout = LinearLayout(context)
          parkingLayout.orientation = LinearLayout.HORIZONTAL
          layoutPark.addView(parkingLayout)
          totalRow++
        }

        when (slotsLayout) {
          SLOT_NULL -> {
            setupParkingView(index, parkingLayout, slotsLayout, R.drawable.ic_blank)
          }
          SLOT_SCAN_ME, SLOT_TAKEN -> {
            totalTakenSlot += 1
            setupParkingView(index, parkingLayout, slotsLayout, R.drawable.ic_car)
          }
          SLOT_EMPTY -> {
            totalEmptySlot += 1
            setupParkingView(index, parkingLayout, slotsLayout, R.drawable.ic_park)
          }
          SLOT_DISABLE -> {
            totalDisableSlot += 1
            setupParkingView(index, parkingLayout, slotsLayout, R.drawable.ic_disable)
          }
          SLOT_ROAD, SLOT_READY -> {
            setupParkingView(index, parkingLayout, slotsLayout, R.color.transparent)
          }
          SLOT_IN -> {
            setupParkingView(index, parkingLayout, slotsLayout, R.drawable.ic_in)
          }
          SLOT_OUT -> {
            setupParkingView(index, parkingLayout, slotsLayout, R.drawable.ic_out)
          }
          SLOT_BLOCK -> {
            setupParkingView(index, parkingLayout, slotsLayout, R.drawable.ic_road)
          }
        }

        if (totalRow == 30) {
          showTotalSlotDetail(totalDisableSlot, totalEmptySlot, totalTakenSlot)
          bindingHome.home.numberingLeftOneRow.visibility = VISIBLE
          bindingHome.home.numberingLeft.visibility = GONE
          bindingHome.home.btnSave.isEnabled = true
          showProgress(false)
        }
        if (totalRow == 31) {
          bindingHome.home.numberingLeftOneRow.visibility = GONE
          bindingHome.home.numberingLeft.visibility = VISIBLE
          bindingHome.home.btnSave.isEnabled = false
          showProgress(true)
        }
        if (totalRow == 60) {
          showTotalSlotDetail(totalDisableSlot, totalEmptySlot, totalTakenSlot)
          bindingHome.home.btnSave.isEnabled = true
          showProgress(false)
        }
      }
    }
  }

  private fun setupParkingView(count: Int, layout: LinearLayout?, code: Char, icon: Int): TextView {
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

      if (icon == R.color.transparent) {
        setTextColor(resources.getColor(R.color.colorPrimaryDark))
        text = ((id % 60) + 1).toString()
      }
      setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
      setOnClickListener { onClick(view) }
    }

    layout?.addView(view)
    return view
  }

  private fun onClick(view: View) {
    if (mode == EDIT_MODE) {
      if (view.id == -1) {
        Toast.makeText(context, getString(R.string.activate_this_section),
            Toast.LENGTH_SHORT).show()
      } else {
        with(bindingHome.home, {
          slotName.text = slotName(view.id)
          Toast.makeText(context, slotName(view.id), Toast.LENGTH_SHORT).show()
          if (levelLayout[view.id] == SLOT_TAKEN) {
            context?.let {
              Utils.simpleDialogMessage(it, String.format(getString(R.string.two_value_comma),
                  getString(R.string.slot_number), view.id),
                  getString(R.string.force_update_message))
            }
          }
          statusDisable.setOnClickListener {
            view.setBackgroundResource(R.drawable.ic_disable)
            changeSlot(levelLayout, SLOT_DISABLE, view.id)
          }
          statusPark.setOnClickListener {
            view.setBackgroundResource(R.drawable.ic_park)
            changeSlot(levelLayout, SLOT_EMPTY, view.id)
          }
          statusRoad.setOnClickListener {
            view.setBackgroundResource(R.color.transparent)
            changeSlot(levelLayout, SLOT_ROAD, view.id)
          }
          statusIn.setOnClickListener {
            view.setBackgroundResource(R.drawable.ic_in)
            changeSlot(levelLayout, SLOT_IN, view.id)
          }
          statusOut.setOnClickListener {
            view.setBackgroundResource(R.drawable.ic_out)
            changeSlot(levelLayout, SLOT_OUT, view.id)
          }
          statusBlock.setOnClickListener {
            view.setBackgroundResource(R.drawable.ic_road)
            changeSlot(levelLayout, SLOT_BLOCK, view.id)
          }
        })
      }
    }
  }

  private fun changeSlot(levelLayout: String, status: Char, index: Int) {
    val levelLayoutStrBuilder = StringBuilder(levelLayout)
    levelLayoutStrBuilder.setCharAt(index, status)
    this@HomeFragment.levelLayout = levelLayoutStrBuilder.toString()
    bindingHome.home.layoutSlotDetailLevel.emptySlotValue.text = (this@HomeFragment.levelLayout.count {
      SLOT_EMPTY.toString().contains(it)
    }).toString()
    bindingHome.home.layoutSlotDetailLevel.disableSlotValue.text = (this@HomeFragment.levelLayout.count {
      SLOT_DISABLE.toString().contains(it)
    }).toString()
    bindingHome.home.layoutSlotDetailLevel.takenSlotValue.text = (this@HomeFragment.levelLayout.count {
      SLOT_TAKEN.toString().contains(it)
    }).toString()
  }

  private fun editMode() {
    if (bindingHome.home.btnEditMode.text != "Edit mode") {
      context?.let {
        AlertDialog.Builder(it).setTitle(getString(R.string.exit_edit_mode_title)).setMessage(
            getString(R.string.exit_edit_mode_descritption)).setPositiveButton(
            android.R.string.yes) { _, _ ->
          showProgress(true)
          bindingHome.home.btnEditMode.isEnabled = false
          bindingHome.home.btnSave.isEnabled = false

          showProgress(true)
          presenter.getParkingLayout(idLevel, accessToken)
          presenter.editModeParkingLevel(idLevel, EXIT_EDIT_MODE, accessToken)
        }.setNegativeButton(android.R.string.no, null).setIcon(R.drawable.ic_arrow).show()
      }
    } else {
      bindingHome.home.btnEditMode.isEnabled = false
      bindingHome.home.btnSave.isEnabled = false

      showProgress(true)
      presenter.getParkingLayout(idLevel, accessToken)
      presenter.editModeParkingLevel(idLevel, EDIT_MODE, accessToken)
    }
  }

  private fun showTotalSlotDetail(totalDisableSlot: Int, totalEmptySlot: Int, totalTakenSlot: Int) {
    with(bindingHome.home.layoutSlotDetailLevel) {
      disableSlotValue.text = totalDisableSlot.toString()
      emptySlotValue.text = totalEmptySlot.toString()
      takenSlotValue.text = totalTakenSlot.toString()
    }
  }

  override fun editModeParkingLevelSuccess(response: String) {
    with(bindingHome.home) {
      if (mode == EDIT_MODE) {
        mode = EXIT_EDIT_MODE
        btnSync.visibility = VISIBLE
        btnAddLevel.visibility = VISIBLE
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        btnEditLevel.visibility = VISIBLE
        levelName.visibility = VISIBLE
        btnEditMode.text = getString(R.string.edit_mode)
        btnSave.visibility = GONE
        btnViewSection.visibility = VISIBLE
        btnEditMode.setTextColor(resources.getColor(R.color.colorPrimary))
        editMode.visibility = GONE
        btnEditMode.visibility = VISIBLE
      } else {
        mode = EDIT_MODE
        if (isSyncOn) {
          isSyncOn = false
          btnSync.setImageResource(R.drawable.ic_sync_off)
          handler.removeCallbacksAndMessages(null)
        }
        btnSync.visibility = INVISIBLE
        btnAddLevel.visibility = INVISIBLE
        btnEditLevel.visibility = INVISIBLE
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        levelName.visibility = GONE
        btnEditMode.text = getString(R.string.exit_edit_mode)
        btnViewSection.visibility = GONE
        btnEditMode.setTextColor(resources.getColor(R.color.red))
        editMode.visibility = VISIBLE
        btnSave.visibility = VISIBLE
        slotName.text = getString(R.string.select_slot)
      }
      btnEditMode.isEnabled = true
    }
  }

  override fun addParkingLevelSuccess(response: String) {
    Toast.makeText(context, getString(R.string.success_create_level), Toast.LENGTH_LONG).show()
    refreshPage()
    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    bindingHome.addLevel.txtLevelName.text?.clear()
    bindingHome.addLevel.cbIHaveRead.isChecked = false
    bindingHome.addLevel.btnCreate.isEnabled = true
  }

  override fun getLayoutSuccess(slotsLayout: String) {
    levelLayout = slotsLayout
    showParkingLayout(slotsLayout)
  }

  override fun getLevelsSuccess(listLevel: List<ListLevel>) {
    spinnerItems.clear()
    spinnerItems.add(SpinnerItem("0", SELECT_LEVEL, "")) // First item
    for (index in listLevel.indices) {
      spinnerItems.add(index + 1, SpinnerItem(listLevel[index].idLevel, listLevel[index].levelName,
          listLevel[index].levelStatus))
    }
    with(bindingHome.home) {
      svParkingSlot.visibility = GONE
      if (listLevel.isEmpty()) {
        tvAddLevel.visibility = VISIBLE
        ivAddLevel.visibility = VISIBLE
      } else {
        ivSelectLevelLeft.visibility = VISIBLE
      }
      tvAppName.visibility = VISIBLE
    }
  }

  override fun getSectionDetailsSuccess(listSectionDetails: List<SectionDetails>) {
    for (index in listSectionDetails.indices) {
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
    with(bindingHome.home) {
      if (btnSection1.text == getString(R.string.activate) && btnSection2.text == getString(
              R.string.activate) && btnSection3.text == getString(
              R.string.activate) && btnSection4.text == getString(
              R.string.activate) || levelStatus == LEVEL_UNAVAILABLE) {
        btnEditMode.visibility = GONE
      } else {
        btnEditMode.visibility = VISIBLE
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
        showProgressSection(true)
        presenter.updateParkingSection(listSectionDetails[index].idSection, accessToken)
      }
    }
  }

  private fun Button.showConfirmationDialog(listSectionDetails: List<SectionDetails>, index: Int) {
    context?.let {
      AlertDialog.Builder(it).setTitle(getString(R.string.deactivate_section)).setMessage(
          getString(R.string.deactivate_section_confirmation)).setPositiveButton(
          android.R.string.yes) { _, _ ->
        showProgressSection(true)
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
    bindingHome.home.btnSave.isEnabled = true
    bindingHome.home.btnEditMode.isEnabled = true
    showProgress(false)
    presenter.editModeParkingLevel(idLevel, EXIT_EDIT_MODE, accessToken)
    Toast.makeText(context, response, Toast.LENGTH_LONG).show()
  }

  override fun updateParkingSectionSuccess(response: String) {
    showProgressSection(false)
    presenter.getSectionDetails(idLevel, accessToken)
  }

  private fun slotName(slotAt: Int): String {
    for (i in 1 .. TOTAL_SLOTS_IN_ROW) {
      if (slotAt < TOTAL_SLOTS_IN_ROW * i) {
        return "(" + LETTER[i - 1] + "-" + ((slotAt % TOTAL_SLOTS_IN_ROW) + 1) + ")"
      }
    }
    return ""
  }

  fun refreshPage() {
    val ft = fragmentManager?.beginTransaction()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      ft?.setReorderingAllowed(false)
    }
    ft?.detach(this)?.attach(this)?.commit()
    bindingHome.home.levelName.setSelection(0)
    adapter.notifyDataSetChanged()
  }

  override fun showProgress(show: Boolean) {
    if (show) {
      bindingHome.home.progressBar.visibility = VISIBLE
    } else {
      bindingHome.home.progressBar.visibility = GONE
    }
  }

  private fun showProgressSection(show: Boolean) {
    if (show) {
      bindingHome.home.progressBarSection.visibility = VISIBLE
    } else {
      bindingHome.home.progressBarSection.visibility = GONE
    }
  }

  override fun showProgressAddLevel(show: Boolean) {
    with(bindingHome.addLevel) {
      if (show) {
        btnCreate.isEnabled = false
        progressBarAddLevel.visibility = VISIBLE
      } else {
        btnCreate.isEnabled = false
        progressBarAddLevel.visibility = GONE
      }
    }
  }

  override fun onFailed(message: String) {
    asyncTask.cancel(true)
    showProgress(false)
    Timber.tag(ERROR).e(message)
    with(bindingHome) {
      home.ibRefresh.visibility = VISIBLE
      home.btnSave.isEnabled = true
      home.btnEditMode.isEnabled = true
      home.ibRefresh.setOnClickListener {
        presenter.getLevels(accessToken)
        home.ibRefresh.visibility = GONE
      }
      if (message.contains(Constants.NO_CONNECTION)) {
        Toast.makeText(context, getString(R.string.no_network_connection),
            Toast.LENGTH_SHORT).show()
      } else {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
      }
      addLevel.btnCreate.isEnabled = true
    }
  }

  override fun onDestroy() {
    if (::asyncTask.isInitialized) {
      asyncTask.cancel(true)
    }
    presenter.detach()
    super.onDestroy()
  }
}