# SmartFarming 
Farmers invest a large amount of time in ensuring their crops are appropriate for distribution to feed our large population. Plant diseases claim 26 per cent of the total crop yield in our country. By detecting these diseases early, they can be tackled and crops can be saved.

Bananas are a commercially important fruit produced in India. Plantations are vast and
banana trees suffer from various diseases like Sigatoka and Aphids which are contagious and cause visible degradation of the fruit. Diseases like Sigatoka cause a loss of up to 50 per cent of produce. Hence, early detection is important.

The aim of this project is to develop an Android mobile system that carries out early disease detection on banana trees in large plantations. Without the use of technology, the same process would require manual intervention, implying increased labour costs.

The project involves the use of a mobile camera which provides an image feed to a control unit. This control unit uses image processing algorithms (Object Detection, ConvexHull, GrabCut etc.) with the help of OpenCV to extract the leaf in examination. Once the leaf is extracted from the image, it is provided to the Random Forest classifier which is trained to identify if the leaf is diseased or not.

Unlike frequently done, the image processing module manages foreground extraction in a real world situation, i.e. even if the background is not plain coloured. The machine learning module makes informed decisions to achieve classification and disease identification. This system increases the convenience of crop monitoring, improving efficiency of the whole process.

The implemented project has identified ~85% of the 120 samples successfully (i.e. whether the image contains diseased or a healthy leaf). The sample contains a mix images that are positive and negative for Sigatoka.

The project can be extended to add more diseases and plants and is a step closer to making farming smarter through the use of machine learning.
