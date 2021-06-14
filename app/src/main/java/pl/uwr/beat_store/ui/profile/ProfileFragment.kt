package pl.uwr.beat_store.ui.profile

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.itextpdf.io.IOException
import com.itextpdf.kernel.colors.Color
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfDocumentInfo
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.LineSeparator
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Text
import com.itextpdf.layout.property.TextAlignment
import pl.uwr.beat_store.R
import pl.uwr.beat_store.viewmodels.LoggedInViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream



class ProfileFragment : Fragment() {

  private lateinit var profileViewModel: ProfileViewModel
  private lateinit var userTextView: TextView;
  private lateinit var logoutButton: Button;
  private lateinit var loggedInViewModel: LoggedInViewModel
  override fun onCreateView(
          inflater: LayoutInflater,
          container: ViewGroup?,
          savedInstanceState: Bundle?
  ): View? {
      profileViewModel =
              ViewModelProvider(this).get(ProfileViewModel::class.java)
      val root = inflater.inflate(R.layout.fragment_profile, container, false)
      val textView: TextView = root.findViewById(R.id.text_profile)
      profileViewModel.text.observe(viewLifecycleOwner, Observer {
        textView.text = it
      })

    loggedInViewModel= ViewModelProviders.of(this).get(LoggedInViewModel::class.java);
      userTextView= root.findViewById(R.id.text_profile);
      logoutButton=  root.findViewById(R.id.logout_button)

      userTextView.text= "Welcome, " + Firebase.auth.currentUser.toString();
    val PERMISSIONS = arrayOf<String>(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    )
    ActivityCompat.requestPermissions(requireActivity(),
            PERMISSIONS,
            1)
      logoutButton.setOnClickListener {
        //loggedInViewModel.logOut();
        //root.findNavController().navigate(R.id.action_navigation_profile_to_loginRegisterFragment);


        createPdf1("o");
        Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
      };
    return root
  }


  private fun getAppPath(context: Context): String? { //for creating pdf
    println("DIRX" + context.packageManager
            .getPackageInfo(context.packageName, 0)
            .applicationInfo.dataDir)
    return context.packageManager
            .getPackageInfo(context.packageName, 0)
            .applicationInfo.dataDir;
  }

  private fun createPdf1(Location: String) {
    val pdfPath: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
    val file: File = File(pdfPath, "myPDF.pdf") //TODO: zrob wiele stron w tym pdfie na kazda licencje
    val outputstream: OutputStream = FileOutputStream(file)
    val writer = PdfWriter(file)
    val pdfDocument : PdfDocument= PdfDocument(writer)
    val document: Document = Document(pdfDocument)
    val info: PdfDocumentInfo = pdfDocument.getDocumentInfo()
    info.title = "Beat"
    info.author = "a@a.pl"
    info.subject = "iText7 PDF "
    info.keywords = "iText"
    info.creator = "s"


    val mColorAccent: Color = DeviceRgb(153, 204, 255)
    val mColorBlack: Color = DeviceRgb(0, 0, 0)
    val mHeadingFontSize = 20.0f
    val mValueFontSize = 26.0f

    val font = PdfFontFactory.createFont("res/font/brandon_medium.otf", "UTF-8", true)

    // LINE SEPARATOR
    val lineSeparator = LineSeparator(DottedLine())
    lineSeparator.setStrokeColor(DeviceRgb(0, 0, 68))

    // Title Order Details...
    // Adding Title....
    val mOrderDetailsTitleChunk: Text = Text("Order Details").setFont(font).setFontSize(36.0f).setFontColor(mColorBlack)
    val mOrderDetailsTitleParagraph: Paragraph = Paragraph(mOrderDetailsTitleChunk)
            .setTextAlignment(TextAlignment.CENTER)
    document.add(mOrderDetailsTitleParagraph)

    // Fields of Order Details...
    // Adding Chunks for Title and value
    val mOrderIdChunk: Text = Text("Order No:").setFont(font).setFontSize(mHeadingFontSize).setFontColor(mColorAccent)
    val mOrderIdParagraph = Paragraph(mOrderIdChunk)
    document.add(mOrderIdParagraph)
    val mOrderIdValueChunk: Text = Text("#TU RANDOM").setFont(font).setFontSize(mValueFontSize).setFontColor(mColorBlack)
    val mOrderIdValueParagraph = Paragraph(mOrderIdValueChunk)
    document.add(mOrderIdValueParagraph)

    // Adding Line Breakable Space....
    document.add(Paragraph(""))
    // Adding Horizontal Line...
    document.add(lineSeparator)
    // Adding Line Breakable Space....
    document.add(Paragraph(""))

    // Fields of Order Details...
    val mOrderDateChunk: Text = Text("Order Date:").setFont(font).setFontSize(mHeadingFontSize).setFontColor(mColorAccent)
    val mOrderDateParagraph = Paragraph(mOrderDateChunk)
    document.add(mOrderDateParagraph)
    val mOrderDateValueChunk: Text = Text("TU DATA").setFont(font).setFontSize(mValueFontSize).setFontColor(mColorBlack)
    val mOrderDateValueParagraph = Paragraph(mOrderDateValueChunk)
    document.add(mOrderDateValueParagraph)
    document.add(Paragraph(""))
    document.add(lineSeparator)
    document.add(Paragraph(""))

    // Fields of Order Details...
    val mOrderAcNameChunk: Text = Text("Account Name:").setFont(font).setFontSize(mHeadingFontSize).setFontColor(mColorAccent)
    val mOrderAcNameParagraph = Paragraph(mOrderAcNameChunk)
    document.add(mOrderAcNameParagraph)
    val mOrderAcNameValueChunk: Text = Text("ACCNAME").setFont(font).setFontSize(mValueFontSize).setFontColor(mColorBlack)
    val mOrderAcNameValueParagraph = Paragraph(mOrderAcNameValueChunk)
    document.add(mOrderAcNameValueParagraph)
    document.add(Paragraph(""))
    document.add(lineSeparator)
    document.add(Paragraph(""))
    document.close()
  }

}