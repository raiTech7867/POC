package com.raitech.assignment

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.raitech.assignment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ValidationHandler{
    lateinit var viewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        viewModel = ViewModelProvider(this)[UserViewModel::class.java]

        binding.viewModel = viewModel
        binding.handler = this

        val tag: String = getString(R.string.desc_text)
        val spannableString = SpannableString(tag)
        spannableString.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.gray_color)),
            0,
            tag.indexOf("."),
            0
        )
        spannableString.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.do_not_have_pan_color)),
            tag.indexOf("."),
            tag.length,
            0
        )
        binding.details.text = spannableString
        changeStatusBarColor()

        viewModel.getLogInResult().observe(this, Observer { result ->

            if (result.equals("Invalid PAN number")) {
                binding.panNumberInvalid.visibility = View.VISIBLE
            } else {
                binding.panNumberInvalid.visibility = View.GONE
            }

            if (binding.birthdate.text.toString().isNotEmpty() && binding.birthMonth.text.toString()
                    .isNotEmpty()
                && binding.birthYear.text.toString().isNotEmpty()
            ) {
                if (result.equals("Invalid Date") || result.equals("Invalid Month") || result.equals(
                        "Invalid Year"
                    )
                ) {
                    binding.invalidDate.visibility = View.VISIBLE
                } else {
                    binding.invalidDate.visibility = View.GONE
                }

            }
        })

        viewModel.isEnable.observe(this, Observer { isEnable ->
            Log.d("MainActivity", "isEnable====" + isEnable)
            binding.nextBtn.isEnabled = isEnable
        })
        binding.panNumberValue.filters = arrayOf<InputFilter>(
            InputFilter.LengthFilter(10),
            InputFilter.AllCaps()
        )
        binding.panNumberValue.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                for (span in s!!.getSpans(0, s!!.length, UnderlineSpan::class.java)) {
                    s!!.removeSpan(span)
                }


            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                viewModel.performValidation()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        binding.birthdate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.performValidation()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        binding.birthMonth.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.performValidation()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        binding.birthYear.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.performValidation()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        binding.doNotHavePan.setOnClickListener {
            finish()
        }
    }

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.gray_color, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(R.color.gray_color)
        }
    }

    override fun nextButtonClicked() {
        Toast.makeText(this, "Details submitted successfully", Toast.LENGTH_SHORT).show()
        finish()
    }

}