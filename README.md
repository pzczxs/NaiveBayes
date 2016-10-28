# NaiveBayes
Classical and Bayesian Naive Bayes Classifiers

1. Introduction
===========

1.1. Description
===========
The naïve Bayes (NB) classifier is a family of simple probabilistic classifiers based on a common assumption that all features are independent of each other, given the category variable. The different NB classifiers differ mainly by the assumptions they make regarding the distribution of features. The assumptions on distribution of features are called event models of the NB classifier [1]. For discrete features, multinomial or Bernoulli distributions are popular. These assumptions lead to two distinct models, which are often confused [2][3]. When dealing with continuous features, a typical assumption is Gaussian distribution. 

Despite apparently over-simplifier assumptions, NB classifier works quite well in many complex real-world applications, such as text classification, keyphrase extraction, medical diagnosis. This paradox is explained by Zhang that true reason for its competitive performance in classification lies in the dependence distribution [4]. In more details, how the local dependence of a feature distributes in each category, evenly or unevenly, and how the local dependencies of all features work together, consistently (supporting a certain category) or inconsistently (cancelling each other out), plays a crucial role.

As one of the most efficient inductive learning algorithms, NB classifier is often used as a baseline in text classification because it is fast and easy to implement. Moreover, with appropriate pre-processing, it is competitive with more advanced methods including support vector machines (SVMs) [2]. However, classical NB classifier, as standardly presented, is not fully Bayesian. At least not in the sense that a posterior distribution over parameters is estimated from training documents and then used for predictive inference for new document. 

1.2. News, Comments, and Bug Reports.
===========
We highly appreciate any suggestion, comment, and bug report.

1.3. License
===========
NaiveBayes is a free JAVA toolbox; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

NaiveBayes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with MLSSVR; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.

3. References
===========
[1]	George H. John and Pat Langley, 1995. Estimating continuous distributions in Bayesian classifiers. Proceedings of the 11th International Conference on Uncertainty in Artificial Intelligence, San Francisco, CA, pp. 338-345. 

[2]	Andrew McCallum and Kamal Nigam, 1998. A comparison of event models for naïve Bayes text classification. ICML/AAAI-98 Workshop on Learning for Text Categorization, AAAI, pp. 41-48. 

[3]	Vangelis Metsis, Ion Androutsopoulos, and Georgios Paliouras, 2006. Spam filtering with naive Bayes – which naive Bayes? The 3rd Conference on Email and Anti-Spam. 

[4]	Harry Zhang, 2004. The optimality of naive Bayes. Proceedings of the 17th International Florida Artificial Intelligence Research Society Conference, AAAI Press, pp. 562-567. 

[5] Shuo Xu, 2016. Bayesian Naive Bayes Classifiers to Text Classification Journal of Information Science. 

