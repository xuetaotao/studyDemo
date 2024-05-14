package com.example.studydemo.androidStudy

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.studydemo.base.setOnSingleClickListener
import com.example.studydemo.databinding.FragmentMediaBinding
import com.example.studydemo.utils.GlideEngine
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MediaFragment : Fragment() {

    private lateinit var binding: FragmentMediaBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediaBinding.inflate(layoutInflater)
        binding.btnPic.setOnSingleClickListener {
            picWithThirdLibrary()
        }
        binding.btnCamera.setOnSingleClickListener {

        }
        return binding.root
    }

    //1.仍然需要手动在AndroidManifest中配置权限声明，但是动态权限申请的代码这个库有了，但是没有权限使用说明等描述，
    // 故而建议还是根据App需求定制写动态权限申请的代码。
    //2.默认是多张选择模式，如果每次选择一张的话，建议更换选择模式setSelectionMode，或者设置最大选择为1个
    private fun picWithThirdLibrary() {
        PictureSelector
            .create(this)
            .openGallery(SelectMimeType.ofImage())
//            .setSelectionMode(SelectModeConfig.SINGLE)
            .setMaxSelectNum(3)
            .isMaxSelectEnabledMask(true)
            .setImageEngine(GlideEngine.createGlideEngine())
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>?) {
                    Log.e("taotao-->", "result.size: ${result?.size}")
                    lifecycleScope.launch {
                        result?.forEach { localMedia ->
                            delay(2000)
                            Log.e("taotao-->", "availablePath: ${localMedia.availablePath}")
                            val uri = Uri.parse(localMedia.availablePath)
                            Glide.with(this@MediaFragment).load(uri).into(binding.ivImage)
                        }
                    }
                }

                override fun onCancel() {
                    Log.e("taotao-->", "onCancel")
                }
            })
    }

    fun cameraWithThirdLibrary() {

    }
}