package com.example.studydemo.androidStudy

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.content.contentValuesOf
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.studydemo.base.setOnSingleClickListener
import com.example.studydemo.databinding.FragmentMediaBinding
import com.example.studydemo.utils.GlideEngine
import com.example.studydemo.utils.showLog
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnCallbackListener
import com.luck.picture.lib.interfaces.OnPermissionDeniedListener
import com.luck.picture.lib.interfaces.OnPermissionDescriptionListener
import com.luck.picture.lib.interfaces.OnPermissionsInterceptListener
import com.luck.picture.lib.interfaces.OnRequestPermissionListener
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.language.LanguageConfig
import com.luck.picture.lib.utils.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class MediaFragment : Fragment() {

    private lateinit var binding: FragmentMediaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showLog("onCreate")
//        pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
    }

    override fun onStart() {
        super.onStart()
        showLog("onStart")
    }

    override fun onResume() {
        super.onResume()
        showLog("onResume")
    }

    override fun onPause() {
        super.onPause()
        showLog("onPause")
    }

    override fun onStop() {
        super.onStop()
        showLog("onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showLog("onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        showLog("onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        showLog("onDetach")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
        binding.btnSystemCamera.setOnSingleClickListener {
            requestCameraPermission {
                openCamera(requireContext())
            }
        }
        binding.btnPhotoPicker.setOnSingleClickListener {
            openAlbumsWithSystemPhotoPicker()
        }
        binding.btnPermission.setOnSingleClickListener {
            requestImagePermission()
        }
        return binding.root
    }

    /**
     * 三方库pictureselector：使用自定义相册，带拍照
     *
     * 1.仍然需要手动在AndroidManifest中配置权限声明，但是动态权限申请的代码这个库有了，但是没有权限使用说明等描述，而且权限申请处理逻辑很无语，
     * 故而建议还是根据App需求定制写动态权限申请的代码。
     * 2.默认是多张选择模式，如果每次选择一张的话，建议更换选择模式setSelectionMode，或者设置最大选择为1个
     * 3.Inject into any view fragment 和 Custom Inject into any view fragment，这两种方式测试总是有问题，暂时不用
     */
    private fun openAlbumWithThirdLibrary() {
        PictureSelector.create(this)
            .openGallery(SelectMimeType.ofImage())
            .setSelectionMode(SelectModeConfig.MULTIPLE)
            .setMaxSelectNum(1)
            .isMaxSelectEnabledMask(true)
            .setImageEngine(GlideEngine.createGlideEngine())
            .setPermissionDeniedListener(object : OnPermissionDeniedListener {
                override fun onDenied(
                    fragment: Fragment?,
                    permissionArray: Array<out String>?,
                    requestCode: Int,
                    call: OnCallbackListener<Boolean>?
                ) {
                    ToastUtils.showToast(fragment?.requireContext(), "相册访问权限被拒绝了")
                }
            })
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
        PictureSelector.create(this).openSystemGallery(SelectMimeType.ofImage())
            .setSelectionMode(SelectModeConfig.MULTIPLE)
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
        PictureSelector.create(this).openCamera(SelectMimeType.ofImage())
            .setLanguage(LanguageConfig.SYSTEM_LANGUAGE)
            .setPermissionDescriptionListener(object : OnPermissionDescriptionListener {
                override fun onPermissionDescription(
                    fragment: Fragment?,
                    permissionArray: Array<out String>?
                ) {
                    ToastUtils.showToast(fragment?.requireContext(), "我需要相机权限啦")
                }

                override fun onDismiss(fragment: Fragment?) {
                    ToastUtils.showToast(fragment?.requireContext(), "我被Dismiss了")
                }
            })
//            .setPermissionsInterceptListener(object : OnPermissionsInterceptListener {
//                override fun requestPermission(
//                    fragment: Fragment?,
//                    permissionArray: Array<out String>?,
//                    call: OnRequestPermissionListener?
//                ) {
//                    ToastUtils.showToast(
//                        fragment?.requireContext(),
//                        "permissionArray: $permissionArray"
//                    )
//                }
//
//                override fun hasPermissions(
//                    fragment: Fragment?,
//                    permissionArray: Array<out String>?
//                ): Boolean {
//                    ToastUtils.showToast(fragment?.requireContext(), "hasPermissions:true")
//                    return true
//                }
//            })
            .setPermissionDeniedListener(object : OnPermissionDeniedListener {
                override fun onDenied(
                    fragment: Fragment?,
                    permissionArray: Array<out String>?,
                    requestCode: Int,
                    call: OnCallbackListener<Boolean>?
                ) {
                    ToastUtils.showToast(fragment?.requireContext(), "相机权限被拒绝了")
                }
            })
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
    private val pickMedia: ActivityResultLauncher<PickVisualMediaRequest> =
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
    private val pickMultipleMedia: ActivityResultLauncher<PickVisualMediaRequest> =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(9)) { uris ->
            // Callback is invoked after the user selects media items or closes the photo picker.
            Log.e("taotao-->", "uris.size: ${uris?.size}")
            val imageDataList = convertToImageData(uris)
            imageDataList?.forEach {
                Log.e("taotao-->", it.toString())
            }
//            lifecycleScope.launch {
//                uris?.forEachIndexed { index, uri ->
//                    if (index != 0) {
//                        delay(2000)
//                    }
//                    Log.e("taotao-->", "uri: $uri")
//                    Glide.with(this@MediaFragment).load(uri).into(binding.ivImage)
//                }
//            }
        }

    private fun convertToImageData(result: List<Uri>?): List<ImageData>? {
        return if (result.isNullOrEmpty()) {
            null
        } else {
            val imageDataList = mutableListOf<ImageData>()
            result.forEach { uri ->
                //TODO 怎么优化一下改成回调
                var fileName = ""
                context?.let { mContext ->
                    lifecycleScope.launch {
                        Log.e("taotao-->", "before getFileNameByUri:${Thread.currentThread().name}")
                        getFileNameByUri(uri, mContext)?.let {
                            fileName = it
                        }
                    }
                }
                Log.e("taotao-->", "imageDataList.add:${Thread.currentThread().name}")
                imageDataList.add(ImageData(uri, fileName))
            }
            imageDataList
        }
    }

    private suspend fun getFileNameByUri(uri: Uri, context: Context): String? {
        var fileName: String? = null
        var cursor: Cursor? = null
        when (uri.scheme) {
            ContentResolver.SCHEME_FILE -> fileName = uri.toFile().name
            ContentResolver.SCHEME_CONTENT -> {
                try {
                    cursor = context.applicationContext.contentResolver.query(
                        uri, null, null, null, null
                    )
                    if (cursor?.moveToFirst() == true) {
                        val idColumnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        fileName = cursor.getString(idColumnIndex)
                    }
                } catch (e: Exception) {
                    Log.e("taotao-->", "get file name by uri exception:${e.localizedMessage}")
                } finally {
                    cursor?.close()
                }
            }
        }
        Log.e(
            "taotao-->",
            "getFileNameByUri:${Thread.currentThread().name}, fileName: $fileName, uri.scheme: ${uri.scheme}, parseId: ${
                ContentUris.parseId(
                    uri
                )
            }"
        )
        return fileName
    }

    private fun openAlbumsWithSystemPhotoPicker() {
        val mediaType = ActivityResultContracts.PickVisualMedia.ImageAndVideo
//        val mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
//        val mediaType = ActivityResultContracts.PickVisualMedia.VideoOnly
//        val mediaType = ActivityResultContracts.PickVisualMedia.SingleMimeType("image/gif")
        pickMultipleMedia.launch(PickVisualMediaRequest(mediaType))
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {

        }
    }

    private fun requestCameraPermission(permissionsGrantedListener: (() -> Unit)? = null) {
        this.permissionsGrantedListener = permissionsGrantedListener
        requestPermissions.launch(arrayOf(Manifest.permission.CAMERA))
    }

    private fun openCamera(context: Context) {
        val mimeType = "image/jpeg"
        val fileName = "IMG_${System.currentTimeMillis()}.jpg"
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = contentValuesOf(
                Pair(MediaStore.MediaColumns.DISPLAY_NAME, fileName),
                Pair(MediaStore.MediaColumns.MIME_TYPE, mimeType),
                Pair(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
            )
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        } else {
            FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName,
                File(context.externalCacheDir, "/$fileName")
            )
        }
        uri?.let {
            takePicture.launch(it)
        }
    }

    private var permissionsGrantedListener: (() -> Unit)? = null

    // Register ActivityResult handler
    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            // Handle permission requests results
            // See the permission example in the Android platform samples: https://github.com/android/platform-samples
            var allPermissionsGranted = true
            run {
                results.entries.forEach {
                    Log.e("taotao-->", "${it.key} is ${it.value}")
                    if (!it.value) {
                        allPermissionsGranted = false
                        return@run
                    }
                }
            }
            if (allPermissionsGranted) {
                permissionsGrantedListener?.invoke()
            }
        }

    private fun requestImagePermission(permissionsGrantedListener: (() -> Unit)? = null) {
        this.permissionsGrantedListener = permissionsGrantedListener
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            //选择部分照片和视频：READ_MEDIA_IMAGES is false, READ_MEDIA_VIDEO is false, READ_MEDIA_VISUAL_USER_SELECTED is true
            //全部允许：READ_MEDIA_IMAGES is true, READ_MEDIA_VIDEO is true, READ_MEDIA_VISUAL_USER_SELECTED is true
            requestPermissions.launch(
                arrayOf(
                    READ_MEDIA_IMAGES, READ_MEDIA_VIDEO, READ_MEDIA_VISUAL_USER_SELECTED
                )
            )
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))
        } else {
            requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
        }
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

    data class ImageData(val uri: Uri, val fileName: String) {
        override fun toString(): String {
            return "{ uri: $uri, fileName: $fileName }"
        }
    }
}