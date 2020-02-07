package com.future.pms.admin.login.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.future.pms.admin.BaseApp
import com.future.pms.admin.R
import com.future.pms.admin.core.base.BaseActivity
import com.future.pms.admin.core.model.Token
import com.future.pms.admin.core.network.Authentication
import com.future.pms.admin.login.injection.DaggerLoginComponent
import com.future.pms.admin.login.injection.LoginComponent
import com.future.pms.admin.login.presenter.LoginPresenter
import com.future.pms.admin.main.view.MainActivity
import com.future.pms.admin.util.Constants
import com.future.pms.admin.util.Constants.Companion.BAD_REQUEST_CODE
import com.future.pms.admin.util.Constants.Companion.FORBIDDEN_CODE
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : BaseActivity(), LoginContract {
  private var daggerBuild: LoginComponent = DaggerLoginComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: LoginPresenter
  @Inject lateinit var gson: Gson

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN)
    setContentView(R.layout.activity_login)
    presenter.attach(this)
    presenter.subscribe()
    btnSign.setOnClickListener {
      if (isValid()) {
        loading(true)
        hideKeyboard()
        presenter.login(txtEmail.text.toString(), txtPassword.text.toString())
      }
    }
  }

  private fun isValid(): Boolean {
    if (txtEmail?.text.toString().isEmpty()) return false
    if (txtPassword?.text.toString().isEmpty()) return false
    return true
  }

  override fun onSuccess(token: Token) {
    Authentication.save(this, token)
    presenter.loadData(gson.fromJson(
        this.getSharedPreferences(Constants.AUTHENTICATION, Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).accessToken)
  }

  override fun onAuthorized() {
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)
    finish()
  }

  private fun hideKeyboard() {
    val view = currentFocus
    view?.let {
      val mInputMethodManager = getSystemService(
          Activity.INPUT_METHOD_SERVICE) as InputMethodManager
      mInputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
  }

  private fun loading(isLoading: Boolean) {
    if (isLoading) {
      inputLayoutEmail.visibility = View.GONE
      progressBar.visibility = View.VISIBLE
      inputLayoutPassword.visibility = View.GONE
      btnSign.visibility = View.GONE
    } else {
      txtPassword.text?.clear()
      progressBar.visibility = View.GONE
      inputLayoutEmail.visibility = View.VISIBLE
      inputLayoutPassword.visibility = View.VISIBLE
      btnSign.visibility = View.VISIBLE
    }
  }

  override fun onFailed(e: String) {
    loading(false)
    Authentication.delete(this)
    if (e.contains(BAD_REQUEST_CODE) || e.contains(FORBIDDEN_CODE) || e.contains(
            getString(R.string.login_activity))) {
      Toast.makeText(this, R.string.email_password_incorrect, Toast.LENGTH_LONG).show()
    } else {
      Toast.makeText(this, e, Toast.LENGTH_LONG).show()
    }
  }

  override fun onBackPressed() {
    super.onBackPressed()
    this.finishAffinity()
  }

  override fun onDestroy() {
    presenter.detach()
    super.onDestroy()
  }
}
