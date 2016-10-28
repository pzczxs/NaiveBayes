package cn.ac.istic.corpus;

/**
 * ISplitCorpus allows a corpus to resize and split a cross validation data set.
 * 
 * @author XU, Shuo (pzczxs@gmail.com)
 * @version 1.0
 * 
 */
public interface ISplitCorpus {

    /**
     * splits two child corpora of size 1/nfold off the original corpus, which
     * itself is left unchanged (except storing the splits). The corpora can be
     * retrieved using getTrainCorpus and getTestCorpus after using this
     * function.
     * 
     * @param nfold number of partitions
     * @param seed random seed
     */
    public void split(final int nfold, final long seed);

    /**
     * called after split()
     * 
     * @return the training corpus according to the last splitting operation
     */
    public ICorpus getTrainCorpus(final int split);

    /**
     * called after split()
     * 
     * @return the test corpus according to the last splitting operation
     */
    public ICorpus getTestCorpus(final int split);
}
