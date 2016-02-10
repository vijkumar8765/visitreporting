<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta name="menu" content="home" />
	<title>Style Sheet Demo</title>
	<style type="text/css">
		.swatch {
			width:  	40px;
			float:  	left;
			display: 	inline-block;
			min-height: 40px;
			font-size:	10pt;
		}
		.swatch p {
			text-align: center;
			vertical-align:middle
		}
	</style>
</head>

<body>


	<div class="swatch" style="background-color: #4C5356" title="Grey"><!-- --></div>
	<div class="swatch" style="background-color: #7F8789" title="Grey 70%"><!-- --></div>
	<div class="swatch" style="background-color: #00405B" title="Petrol"><!-- --></div>
	<div class="swatch" style="background-color: #33667C" title="Petrol 80%"><!-- --></div>
	<div class="swatch" style="background-color: #668C9D" title="Petrol 60%"><!-- --></div>
	<div class="swatch" style="background-color: #BBBCBE" title="Silver"><!-- --></div>
	<div class="swatch" style="background-color: #E4E4E5" title="Silver 40%"><!-- --></div>
	<div class="swatch" style="background-color: #CFE7E7" title="Petrol Light"><!-- --></div>
	<div class="swatch" style="background-color: #E7F3F3" title="Petrol Light 50%"><!-- --></div>
	<div class="swatch" style="background-color: #9A0C39" title="Red"><!-- --></div>
	<div class="swatch" style="background-color: #C26D88" title="Red 60%"><!-- --></div>
	<div class="swatch" style="background-color: #EBCED7" title="Red 20%"><!-- --></div>
	<div class="swatch" style="background-color: #F6E5BC" title="Sand"><!-- --></div>
	<div class="swatch" style="background-color: #FAF2DE" title="Sand 50%"><!-- --></div>
	<div class="swatch" style="background-color: #FFFFFF" title="White"><!-- --></div>

	<div style="clear:both">.</div>
	<hr />
	<h1>H1 Title</h1>
	<h2>H2 Title <a href="#">and link</a></h2>
	<h3>H3 Title <a href="#">and link</a></h3>
	<h4>H4 Title <a href="#">and link</a></h4>
	<h5>H5 Title <a href="#">and link</a></h5>
	<h6>H6 Title <a href="#">and link</a></h6>
	<hr />
	<p>Here is an example of some paragraph text <a href="#">and link</a>.  <abbr title="Visit Reporting">VR</abbr> is the abbreviated name of the application.  Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum</p>
	<hr />
	<p><strong>This is strong</strong> paragraph text <a href="#">and link</a>. <small>This is some small text <a href="#">and link</a></small>. <em>This text has been emphasized</em> s simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum</p>

	<hr />
	<h4>Ordered List</h4>
	<ol>
		<li>One</li>
		<li>Two </li>
		<li>Three</li>
		<li>Four</li>
		<li>Five</li>
	</ol>
	<hr />
	<h4>Unordered List</h4>
	<ul>
		<li>One</li>
		<li>Two </li>
		<li>Three</li>
		<li>Four</li>
		<li>Five</li>
	</ul>

	<hr />
	<table summary="This is an example table">
		<caption>This is an example table caption</caption>
		<thead>
			<tr>
				<th>Name</th>
				<th>Hometown</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>Row 1 Data 1</td>
				<td>Row 1 Data 2</td>
			</tr>
			<tr>
				<td>Row 2 Data 1</td>
				<td>Row 2 Data 2</td>
			</tr>
		</tbody>
	</table>

	<hr />

	<form>

	 	<fieldset>
			<legend>Some text inputs</legend>
			<div class="clearfix">
				<label>Text</label>
				<div class="input">
					<input type="text" />
				</div>
			</div>


			<div class="clearfix">
				<label>Text</label>
				<div class="input">
					<input type="password" />
				</div>
			</div>

		</fieldset>

		<fieldset>
			<legend>Some other controls</legend>
			<div class="clearfix">
			<label>Text Area</label>
				<div class="input">
					<textarea>This is a textarea</textarea>
				</div>
			</div>

			<input type="checkbox" /><br />
			<label>Text<select name="browser">
				<optgroup label="Brands">
		      		<option label="VW">VW</option>
			     	<option label="Audi">Audi</option>
			      	<option label="VWCV">VWCV</option>
		      		<option label="Seat">Seat</option>
		      		<option label="Skoda">Skoda</option>
				</optgroup>
				<option label="Retailers">Retailers</option>
				<option label="VWFS">VWFS</option>
				</select>
			</label>
		</fieldset>
		<div class="actions">
			<input type="submit" value="Submit" />
			<input type="button" value="Button" />
			<input type="reset" value="Reset" />
			
			<a class="btn" href="#">Link Button</a>
		</div>
	</form>
	<hr />


</body>
</html>

