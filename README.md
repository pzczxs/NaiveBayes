# NaiveBayes
Classical and Bayesian Naive Bayes Classifiers

## Introduction

### 1.1. Description
The naïve Bayes (NB) classifier is a family of simple probabilistic classifiers based on a common assumption that all features are independent of each other, given the category variable. The different NB classifiers differ mainly by the assumptions they make regarding the distribution of features. The assumptions on distribution of features are called event models of the NB classifier [1]. For discrete features, multinomial or Bernoulli distributions are popular. These assumptions lead to two distinct models, which are often confused [2][3]. When dealing with continuous features, a typical assumption is Gaussian distribution. 

Despite apparently over-simplifier assumptions, NB classifier works quite well in many complex real-world applications, such as text classification, keyphrase extraction, medical diagnosis. This paradox is explained by Zhang that true reason for its competitive performance in classification lies in the dependence distribution [4]. In more details, how the local dependence of a feature distributes in each category, evenly or unevenly, and how the local dependencies of all features work together, consistently (supporting a certain category) or inconsistently (cancelling each other out), plays a crucial role.

As one of the most efficient inductive learning algorithms, NB classifier is often used as a baseline in text classification because it is fast and easy to implement. Moreover, with appropriate pre-processing, it is competitive with more advanced methods including support vector machines (SVMs) [2]. However, classical NB classifier, as standardly presented, is not fully Bayesian. At least not in the sense that a posterior distribution over parameters is estimated from training documents and then used for predictive inference for new document. Therefore, several fully Bayesian NB classifiers were proposed in our paper [5]. This is a launch for the distribution of the source code from our paper [5], including classical and Bayesian naive Bayes classifiers with multinomial, Bernoulli and Gaussian event models. 

### 1.2. News, Comments, and Bug Reports.
We highly appreciate any suggestion, comment, and bug report.

#### 1.3. License
Code (c) 2011 Jacob Eisenstein
[Licensed under the Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

## 2. How to Use NaiveBayes

### 2.1. Data Format
Similar to [LIBSVM](https://www.csie.ntu.edu.tw/~cjlin/libsvm/), The format of training and testing data file is:

\<label\> \<index1\>:\<value1\> \<index2\>:\<value2\> ...
.
.
.

Each line contains an instance and is ended by a '\n' character.  \<label\> is a string indicating the category label. The pair \<index\>:\<value\> gives a feature (attribute) value: \<index\> is an integer starting from 0 and \<value\> is a real number. Indices
must be in ASCENDING order. Labels in the testing file are only used to calculate accuracy or errors. If they are unknown, just fill the
first column with any string.

The `Converter.java` in the package `cn.ac.istic.converters` can convert the [data set](http://ana.cachopo.org/datasets-for-single-label-text-categorization) to the required data format. 

### 2.2. How to Use
As for training and testing procedure: 

* Bayesian Naive Bayes Classifiers
    * Multinomial Event Model: Please refer to `BayesianMultinomialNBUI.java` in the package `cn.ac.istic.ui`.
    * Bernoulli Event Model: Please refer to `BayesianBernoulliNBUI.java` in the package `cn.ac.istic.ui`.
    * Gaussian Event Model: Please refer to `BayesianGaussianNBUI.java` in the package `cn.ac.istic.ui`.

* Classical Naive Bayes Classifiers
    * Multinomial Event Model: Please refer to `MultinomialNBUI.java` in the package `cn.ac.istic.ui`.
    * Bernoulli Event Model: Please refer to `BernoulliNBUI.java` in the package `cn.ac.istic.ui`.
    * Gaussian Event Model: Please refer to `GaussianNBUI.java` in the package `cn.ac.istic.ui`.

In order to tune resulting parameters with k-fold cross validation, you can refer to the JAVA files in the package `cn.ac.istic.cv`.

### 2.3. Additional Information
This tool is written by XU, Shuo from Institute of Scientific and Technical Information of China (ISTIC). If you find this tool useful, please cite NaiveBayes as follows

Shuo Xu, 2016. [Bayesian Naive Bayes Classifiers to Text Classification](http://jis.sagepub.com/content/early/2016/11/14/0165551516677946.abstract). *Journal of Information Science*. DOI: http://doi.org/10.1177/0165551516677946. 

For any question, please contact XU, Shuo xush@istic.ac.cn OR pzczxs@gmail.com.

## 3. References
[1]	George H. John and Pat Langley, 1995. Estimating continuous distributions in Bayesian classifiers. *Proceedings of the 11th International Conference on Uncertainty in Artificial Intelligence*, San Francisco, CA, pp. 338-345. 

[2]	Andrew McCallum and Kamal Nigam, 1998. A comparison of event models for naïve Bayes text classification. *ICML/AAAI-98 Workshop on Learning for Text Categorization*, AAAI Press, pp. 41-48. 

[3]	Vangelis Metsis, Ion Androutsopoulos, and Georgios Paliouras, 2006. Spam filtering with naive Bayes – which naive Bayes? *The 3rd Conference on Email and Anti-Spam*. 

[4]	Harry Zhang, 2004. The optimality of naive Bayes. *Proceedings of the 17th International Florida Artificial Intelligence Research Society Conference*, AAAI Press, pp. 562-567. 

[5] Shuo Xu, 2016. [Bayesian Naive Bayes Classifiers to Text Classification](http://jis.sagepub.com/content/early/2016/11/14/0165551516677946.abstract). *Journal of Information Science*. DOI: http://doi.org/10.1177/0165551516677946. 

