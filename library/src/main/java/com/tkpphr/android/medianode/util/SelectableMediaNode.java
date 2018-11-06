package com.tkpphr.android.medianode.util;

import com.tkpphr.android.medianode.core.MediaNode;

public interface SelectableMediaNode<T extends MediaNode<T>> extends MediaNode<T>{
	boolean isSelectable();
}
