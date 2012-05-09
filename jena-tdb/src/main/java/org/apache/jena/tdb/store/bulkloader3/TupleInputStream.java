/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.tdb.store.bulkloader3;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.openjena.atlas.AtlasException;
import org.openjena.atlas.lib.Closeable;
import org.openjena.atlas.lib.Tuple;

public class TupleInputStream implements Iterator<Tuple<Long>>, Closeable {

    private DataInputStream in ;
    private int size ;
    private Tuple<Long> slot = null ;
    
    public TupleInputStream(InputStream in, int size) {
        this.in = DataStreamFactory.createDataInputStream(in) ;
        this.size = size ;
        slot = readNext() ;
    }

    @Override
    public boolean hasNext() {
        return slot != null ;
    }

    @Override
    public Tuple<Long> next() {
        Tuple<Long> result = slot ;
        slot = readNext() ;
        return result ;
    }
    
    private Tuple<Long> readNext() {
        try {
            if ( size == 3 ) {
                return Tuple.create(in.readLong(), in.readLong(), in.readLong()) ;
            } else if ( size == 4 ) {
                return Tuple.create(in.readLong(), in.readLong(), in.readLong(), in.readLong()) ;
            } else {
                throw new AtlasException("Unsupported size.") ;
            }
        } catch (IOException e) {
            return null ;
        }
    }

    @Override
    public void remove() {
        throw new AtlasException("Method not implemented.") ;
    }

    @Override
    public void close() {
        try {
            in.close() ;
        } catch (IOException e) {
            new AtlasException(e) ;
        }        
    }
    
}
