## First steps to running a Jupyter notebook

### Create / edit / run a notebook locally

  1. Download and install [Anaconda] (https://www.continuum.io/downloads)  
  Recommended version: Python 3.6.  
  
  This should also install Python, R, a package and environment manager (conda) and *a lot* of data science packages (like NumPy, Pandas, MatPlotLib, SciKit-Learn etc).  
  Read more in the [Anaconda cheat sheet](https://docs.continuum.io/_downloads/Anaconda_CheatSheet.pdf) or on the [Official Anaconda Documentation site](https://docs.continuum.io/).

  3. Test it.
   * Go to the location where you will store your notebooks.
   * Open a command line tool.
   * Type `jupyter notebook`.
   * Wait a little :).  
     You should see in your browser a tree-like structure of the current folder. 
   * Now you can either:
      - Create a new notebook (upper-right corner: New -> Python 3), or
      - Use one of the notebooks that were presented in previous sessions. You can find some of them on the [confluence page](https://conf.endava.com/display/MLP/Machine+Learning+Practice+Home).
   * Edit and run cells. To edit a cell, hit Enter. To run it, Ctrl + Enter.
     More hot keys can be found here on the [Jupyter Notebook site](http://jupyter-notebook.readthedocs.io/en/latest/notebook.html#keyboard-shortcuts).

### Display those cool wordclouds

  1. Open a command line tool and type  
  `pip install wordcloud`.
  
  2. In case you get an error stating that `Microsoft Visual C++ 14.0 is required. Get it with "Microsoft Visual C++ Build Tools": : http://landinghub.visualstudio.com/visual-cpp-build-tools` , download and install the Microsoft Visual C++ Build Tools.  
  Repeat step 1.
  
  3. Test it.  
  You can either use [Eugen's notebook on classification](https://github.com/eugen/mlstudy/tree/master/3_logistic_regression) to display the same wordclouds, or create your own.  

### Share a notebook online

In order to share the notebook even with colleagues that cannot run Jupyter notebooks, you can do one of the following:

  * Upload it on your github account.
     Github has Jupyter notebook support, that is, the notebook can be read as it was when uploaded (i.e. with the same output cells displayed).
  * Upload it on Anaconda Cloud. In order to do that, you'll have to install the Anaconda Cloud client and create an Anaconda Cloud account:
     - Go to the [Anaconda Cloud login page](https://anaconda.org) and create a user.
     - Open a CLI.
     - Type `$ pip install git+https://github.com/Anaconda-Server/anaconda-client`
     - Type `anaconda login`
     - After having logged in, type `anaconda notebook upload name-of-the-notebook.ipynb`
     - Done.
  * Save the notebook as an .html file (File -> Download as) and, e.g., upload it on Confluence.
   
### Other resources

[Conda cheat sheet](http://conda.pydata.org/docs/using/cheatsheet.html)
