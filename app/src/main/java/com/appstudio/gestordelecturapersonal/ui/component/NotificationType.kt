package com.appstudio.gestordelecturapersonal.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.appstudio.gestordelecturapersonal.ui.theme.*

sealed class NotificationType(
    val backgroundColor: Color,
    val contentColor: Color,
    val icon: ImageVector
) {
    object Syncing : NotificationType(primaryContainerLight, onPrimaryContainerLight, Icons.Default.Sync)
    object Online : NotificationType(primaryLight, onPrimaryLight, Icons.Default.CloudDone)
    object Offline : NotificationType(errorLight, onErrorLight, Icons.Default.CloudOff)
    object Saved : NotificationType(primaryLight, onPrimaryLight, Icons.Default.CheckCircle)
    object Deleted : NotificationType(errorContainerLight, onErrorContainerLight, Icons.Default.Delete)
    object Error : NotificationType(errorContainerLight, onErrorContainerLight, Icons.Default.Error)
    object Success : NotificationType(primaryLight, onPrimaryLight, Icons.Default.CheckCircle)
}