package permissions.dispatcher.ktx

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import permissions.dispatcher.PermissionRequest

internal typealias Fun = () -> Unit
internal typealias ShowRationaleFun = (PermissionRequest) -> Unit
internal typealias ViewType = Either<FragmentActivity, Fragment>
internal typealias ActivityType = Either.Left<FragmentActivity, Fragment>
internal typealias FragmentType = Either.Right<FragmentActivity, Fragment>
