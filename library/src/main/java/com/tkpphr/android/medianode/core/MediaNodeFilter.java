package com.tkpphr.android.medianode.core;

import java.io.Serializable;

public interface MediaNodeFilter<T extends MediaNode<?>> extends Serializable{
	 boolean apply(T mediaNode);
}
