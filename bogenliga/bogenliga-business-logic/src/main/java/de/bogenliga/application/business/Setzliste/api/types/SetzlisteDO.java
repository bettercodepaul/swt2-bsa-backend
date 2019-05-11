package de.bogenliga.application.business.Setzliste.api.types;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class SetzlisteDO extends CommonDataObject {
    private static final long serialVersionUID = 3872635461141356788L;

    private Long wettkampfId;
    List<MatchDO> MatchDOList = new List<MatchDO>() {
        @Override
        public int size() {
            return 0;
        }


        @Override
        public boolean isEmpty() {
            return false;
        }


        @Override
        public boolean contains(Object o) {
            return false;
        }


        @Override
        public Iterator<MatchDO> iterator() {
            return null;
        }


        @Override
        public Object[] toArray() {
            return new Object[0];
        }


        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }


        @Override
        public boolean add(MatchDO matchDO) {
            return false;
        }


        @Override
        public boolean remove(Object o) {
            return false;
        }


        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }


        @Override
        public boolean addAll(Collection<? extends MatchDO> c) {
            return false;
        }


        @Override
        public boolean addAll(int index, Collection<? extends MatchDO> c) {
            return false;
        }


        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }


        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }


        @Override
        public void clear() {

        }


        @Override
        public MatchDO get(int index) {
            return null;
        }


        @Override
        public MatchDO set(int index, MatchDO element) {
            return null;
        }


        @Override
        public void add(int index, MatchDO element) {

        }


        @Override
        public MatchDO remove(int index) {
            return null;
        }


        @Override
        public int indexOf(Object o) {
            return 0;
        }


        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }


        @Override
        public ListIterator<MatchDO> listIterator() {
            return null;
        }


        @Override
        public ListIterator<MatchDO> listIterator(int index) {
            return null;
        }


        @Override
        public List<MatchDO> subList(int fromIndex, int toIndex) {
            return null;
        }
    };

    public SetzlisteDO (Long wettkampfId, List<MatchDO> MatchDOList){
        setWettkampfId(wettkampfId);
        this.MatchDOList = MatchDOList;
    }


    public Long getWettkampfId() {
        return wettkampfId;
    }

    public void setWettkampfId(Long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }
}
