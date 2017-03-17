
<html>
<center>
    <?php 
        $random = $_GET['source'];
        $files = scandir('output/'.$random);
        foreach($files as $file) {
            if(is_dir('output/'.$random.'/'.$file)) continue;
            if(substr($file,6,3)=='txt') {
                echo(file_get_contents('output/'.$random.'/'.$file).'<br/>');
            } else {
    ?>
    
    <img src="<?php echo('output/'.$random.'/'.$file); ?>" width="500px"></br>
    
    <?php   
            }
        }
    ?>
 </center>
 </html>