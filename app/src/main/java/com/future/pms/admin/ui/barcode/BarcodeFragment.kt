package com.future.pms.admin.ui.barcode

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.future.pms.admin.R
import com.future.pms.admin.databinding.FragmentBarcodeBinding
import com.future.pms.admin.di.component.DaggerFragmentComponent
import com.future.pms.admin.di.module.FragmentModule
import com.future.pms.admin.model.oauth.Token
import com.future.pms.admin.util.Constants
import com.future.pms.admin.util.Constants.Companion.BARCODE_FRAGMENT
import com.google.gson.Gson
import javax.inject.Inject

class BarcodeFragment : Fragment(), BarcodeContract {
    @Inject
    lateinit var presenter: BarcodePresenter
    private lateinit var binding: FragmentBarcodeBinding

    companion object {
        const val TAG: String = BARCODE_FRAGMENT
    }

    fun newInstance(): BarcodeFragment {
        return BarcodeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_barcode, container, false)
        with(binding) {
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

    private fun injectDependency() {
        val profileComponent = DaggerFragmentComponent.builder().fragmentModule(
            FragmentModule()
        ).build()
        profileComponent.inject(this)
    }
}