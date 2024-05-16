package com.example.studydemo.androidStudy

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.studydemo.base.setOnSingleClickListener
import com.example.studydemo.databinding.FragmentMediaBinding
import com.example.studydemo.utils.GlideEngine
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.language.LanguageConfig
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
            openAlbumWithThirdLibrary()
        }
        binding.btnPicSystem.setOnSingleClickListener {
            openSystemAlbumWithThirdLibrary()
        }
        binding.btnCamera.setOnSingleClickListener {
            openCameraWithThirdLibrary()
        }
        binding.btnPhotoPicker.setOnSingleClickListener {
            openAlbumsWithSystemPhotoPicker()
        }
        return binding.root
    }

    /**
     * 三方库pictureselector：使用自定义相册，带拍照
     *
     * 1.仍然需要手动在AndroidManifest中配置权限声明，但是动态权限申请的代码这个库有了，但是没有权限使用说明等描述，故而建议还是根据App需求定制写动态权限申请的代码。
     * 2.默认是多张选择模式，如果每次选择一张的话，建议更换选择模式setSelectionMode，或者设置最大选择为1个
     * 3.Inject into any view fragment 和 Custom Inject into any view fragment，这两种方式测试总是有问题，暂时不用
     */
    private fun openAlbumWithThirdLibrary() {
        PictureSelector
            .create(this)
            .openGallery(SelectMimeType.ofImage())
//            .setSelectionMode(SelectModeConfig.SINGLE)
            .setMaxSelectNum(3)
            .isMaxSelectEnabledMask(true)
            .setImageEngine(GlideEngine.createGlideEngine())
            .setLanguage(LanguageConfig.SYSTEM_LANGUAGE)
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>?) {
                    displayPictures(result)
                }

                override fun onCancel() {}
            })
    }

    /**
     * 三方库pictureselector：使用系统的相册
     */
    private fun openSystemAlbumWithThirdLibrary() {
        PictureSelector
            .create(this)
            .openSystemGallery(SelectMimeType.ofImage())
            .setSelectionMode(SelectModeConfig.SINGLE)
            .forSystemResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>?) {
                    displayPictures(result)
                }

                override fun onCancel() {}
            })
    }

    /**
     * 三方库pictureselector：拍照
     *
     * To take photos separately in the Navigation Fragment scene
     */
    private fun openCameraWithThirdLibrary() {
        PictureSelector.create(this)
            .openCamera(SelectMimeType.ofImage())
            .setLanguage(LanguageConfig.SYSTEM_LANGUAGE)
            .forResultActivity(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>?) {
                    displayPictures(result)
                }

                override fun onCancel() {
                    Log.e("taotao-->", "cameraWithThirdLibrary: onCancel")
                }
            })
    }

    private fun displayPictures(result: ArrayList<LocalMedia>?) {
        Log.e("taotao-->", "result.size: ${result?.size}")
        lifecycleScope.launch {
            result?.forEachIndexed { index, localMedia ->
                if (index != 0) {
                    delay(2000)
                }
                val uri = Uri.parse(localMedia.availablePath)
                Log.e(
                    "taotao-->", "availablePath: ${localMedia.availablePath}, uri: $uri"
                )
                Glide.with(this@MediaFragment).load(uri).into(binding.ivImage)
            }
        }
    }

    /**
     * 官方原生的Photo picker: https://developer.android.com/training/data-storage/shared/photopicker?hl=zh-cn
     *
     * 该工具会自动更新，让应用用户能够长期使用扩展的功能，而无需更改任何代码。为了简化照片选择器的集成，请添加 1.7.0 版或更高版本的 androidx.activity 库。
     *
     * 在选择图片和视频时提供一致的体验，同时还可以加强用户隐私保护，而无需请求任何存储权限(无需在AndroidManifest中配置权限声明，进行动态权限申请)。
     *
     * 您可以通过调用 ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(context) 来验证照片选择器在给定设备上是否可用。
     */
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            //选择单个媒体文件, Callback is invoked after the user selects a media item or closes the photo picker.
            if (uri != null) {
                Log.e("taotao-->PhotoPicker", "Selected URI: $uri")
                Glide.with(this@MediaFragment).load(uri).into(binding.ivImage)
            } else {
                Log.e("taotao-->PhotoPicker", "No media selected")
            }
        }

    private fun openAlbumWithSystemPhotoPicker() {
        val mediaType = ActivityResultContracts.PickVisualMedia.ImageAndVideo
//        val mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
//        val mediaType = ActivityResultContracts.PickVisualMedia.VideoOnly
//        val mediaType = ActivityResultContracts.PickVisualMedia.SingleMimeType("image/gif")
        pickMedia.launch(PickVisualMediaRequest(mediaType))
    }

    //平台会限制您可以让用户在照片选择器中选择的文件数量上限。如需访问此限制，请调用 getPickImagesMaxLimit()。 在不支持照片选择器的设备上，系统会忽略此上限。
    private val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
            // Callback is invoked after the user selects media items or closes the photo picker.
            Log.e("taotao-->", "uris.size: ${uris?.size}")
            lifecycleScope.launch {
                uris?.forEachIndexed { index, uri ->
                    if (index != 0) {
                        delay(2000)
                    }
                    Log.e("taotao-->", "uri: $uri")
                    Glide.with(this@MediaFragment).load(uri).into(binding.ivImage)
                }
            }
        }

    private fun openAlbumsWithSystemPhotoPicker() {
        val mediaType = ActivityResultContracts.PickVisualMedia.ImageAndVideo
//        val mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
//        val mediaType = ActivityResultContracts.PickVisualMedia.VideoOnly
//        val mediaType = ActivityResultContracts.PickVisualMedia.SingleMimeType("image/gif")
        pickMultipleMedia.launch(PickVisualMediaRequest(mediaType))
    }

    /**
     * 请求权限拒绝后跳转app应用信息详情页，让用户手动开启
     */
    private fun gotoAppDetailsSetting(context: Context) {
        val intent = Intent()
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.setData(uri)
        context.startActivity(intent)
    }
}